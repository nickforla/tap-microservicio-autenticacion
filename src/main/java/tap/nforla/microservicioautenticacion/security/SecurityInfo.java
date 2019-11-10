package tap.nforla.microservicioautenticacion.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SecurityInfo {

    @Value("${TAP_JWT_SECRET}")
    private String jwtSecret;
    @Value("${gateway.security.authUrl}")
    private String authUrl;
    @Value("${gateway.security.tokenType}")
    private String tokenType;
    @Value("${gatwway.security.tokenIssuer}")
    private String tokenIssuer;
    @Value("${gateway.security.tokenAudience}")
    private String tokenAudience;
    @Value("${gateway.security.expiresIn}")
    private long expiresIn;

}
