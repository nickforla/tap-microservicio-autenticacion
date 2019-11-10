package tap.nforla.microservicioautenticacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tap.nforla.microservicioautenticacion.exceptions.JwtNoPresenteException;
import tap.nforla.microservicioautenticacion.services.IUsuarioService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/requests")
public class RequestsController {

    private final Logger logger = LoggerFactory.getLogger(RequestsController.class);
    private IUsuarioService usuarioService;
    private ObjectMapper objectMapper;

    public RequestsController(IUsuarioService usuarioService, ObjectMapper objectMapper) {
        this.usuarioService = usuarioService;
        this.objectMapper = objectMapper;
    }

    @GetMapping(path = "/cuotaMaximaPorHora", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCuotaMaxima(HttpServletRequest request){

        try{

            String username = usuarioService.getUsernameFromJwt(request.getHeader("Authorization"));

            int cuotaMaximaRequestsPorHora = usuarioService.getCuotaMaximaRequestsPorHora(username);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("username", username);
            responseBody.put("cuotaMaximaRequestsPorHora", cuotaMaximaRequestsPorHora);

            return ResponseEntity.ok(objectMapper.writeValueAsString(responseBody));

        }catch (UsernameNotFoundException exc){

            logger.warn(exc.getMessage());
            return ResponseEntity.ok(String.format("{\"mensaje\": \"%s\"}", exc.getMessage()));

        }catch (JwtNoPresenteException | IOException exc){

            logger.error("Excepción en la ejecución: " + exc.getMessage());

            return ResponseEntity.badRequest().body("{\"mensaje\": \"La solicitud no es válida\"}");

        }
    }
}
