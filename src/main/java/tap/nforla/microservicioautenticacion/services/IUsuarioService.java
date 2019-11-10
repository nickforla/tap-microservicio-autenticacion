package tap.nforla.microservicioautenticacion.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tap.nforla.microservicioautenticacion.exceptions.JwtNoPresenteException;

import java.io.IOException;

public interface IUsuarioService {

    String getUsernameFromJwt(String jwtToken) throws JwtNoPresenteException, IOException;
    int getCuotaMaximaRequestsPorHora(String username) throws UsernameNotFoundException;

}
