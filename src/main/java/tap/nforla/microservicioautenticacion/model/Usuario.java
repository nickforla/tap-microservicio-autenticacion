package tap.nforla.microservicioautenticacion.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(value = "usuarios")
public class Usuario {

    @Id
    @Field(name = "_id")
    private ObjectId id;
    private String username;
    private String password;
    private int maxRequestsPorHora;
    private Rol rol;

}
