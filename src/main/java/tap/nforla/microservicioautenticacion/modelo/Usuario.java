package tap.nforla.microservicioautenticacion.modelo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(value = "usuarios")
public class Usuario {

    private String username;
    private String password;
    private int maxRequestsPorHora;
    private Rol rol;

}
