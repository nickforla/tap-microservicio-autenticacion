package tap.nforla.microservicioautenticacion.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import tap.nforla.microservicioautenticacion.exceptions.JwtNoPresenteException;
import tap.nforla.microservicioautenticacion.model.Usuario;
import tap.nforla.microservicioautenticacion.repo.UsuarioRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService, IUsuarioService {

    private UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuario> userOptional = usuarioRepository.findByUsername(username);

        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("El usuario no ha sido encontrado..");
        }

        Usuario usuario = userOptional.get();

        return new User(usuario.getUsername(), usuario.getPassword(), Arrays.asList(new SimpleGrantedAuthority(usuario.getRol().name())));
    }

    public int getCuotaMaximaRequestsPorHora(String username) throws UsernameNotFoundException{

        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);

        return usuarioOptional.orElseThrow(()-> new UsernameNotFoundException("El nombre de usuario indicado no corresponde con ningún usuario")).getMaxRequestsPorHora();

    }

    public String getUsernameFromJwt(String jwtToken) throws JwtNoPresenteException, IOException {

        if(jwtToken == null){

            throw new JwtNoPresenteException("JWT no está presente en el header del request");

        }

        Map<String, Object> jwtPayload = getJwtPayload(jwtToken.replace("Bearer ", ""));

        return (String)jwtPayload.get("sub");

    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getJwtPayload(String jwtToken) throws IOException {

        String payload = jwtToken.split("\\.")[1];

        return new ObjectMapper().readValue(Base64Utils.decodeFromString(payload), Map.class);

    }
}