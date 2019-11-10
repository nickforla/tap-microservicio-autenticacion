package tap.nforla.microservicioautenticacion.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tap.nforla.microservicioautenticacion.services.UsuarioService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private AuthenticationManager authenticationManager;
    private SecurityInfo securityInfo;
    private UsuarioService usuarioService;
    private ObjectMapper objectMapper;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, SecurityInfo securityInfo,
                                   UsuarioService usuarioService, ObjectMapper objectMapper){
        this.authenticationManager = authenticationManager;
        this.securityInfo = securityInfo;
        this.usuarioService = usuarioService;
        this.objectMapper = objectMapper;

        setFilterProcessesUrl(securityInfo.getAuthUrl());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try{

            logger.info("Recibida intento de autenticación");

            String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

            if(!json.equals("")){

                JsonParser jsonParser = JsonParserFactory.getJsonParser();

                Map<String, Object> credentials = jsonParser.parseMap(json);

                String username = (String)credentials.get("username");
                String password = (String)credentials.get("password");

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

                return authenticationManager.authenticate(authenticationToken);

            }

            throw new AuthenticationCredentialsNotFoundException("Credenciales vacías");

        }catch (IOException exc){

            logger.error("Excepción durante autenticación: " + exc.getMessage());
            throw new AuthenticationServiceException("Hubo una excepción al intentar obtener el body del request..");

        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal();

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        byte[] signingKey = securityInfo.getJwtSecret().getBytes();

        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS256)
                .setHeaderParam("typ", securityInfo.getTokenType())
                .setIssuer(securityInfo.getTokenIssuer())
                .setAudience(securityInfo.getTokenAudience())
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + securityInfo.getExpiresIn())) //10 días
                .claim("rol", roles)
                .claim("cta", usuarioService.getCuotaMaximaRequestsPorHora(user.getUsername()))
                .compact();

        Map<String, Object> authResponse = new HashMap<>();
        authResponse.put("token", token);
        authResponse.put("expires_in", securityInfo.getExpiresIn());

        response.getOutputStream().print(objectMapper.writeValueAsString(authResponse));
        response.setContentType("application/json;charset=UTF-8");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("mensaje", "Usuario o contraseña no válidos..");

        response.getOutputStream().print(objectMapper.writeValueAsString(responseBody));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
    }
}
