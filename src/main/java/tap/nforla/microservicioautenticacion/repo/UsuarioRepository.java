package tap.nforla.microservicioautenticacion.repo;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;
import tap.nforla.microservicioautenticacion.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, ObjectId> {

    Optional<Usuario> findByUsername(String username);
}
