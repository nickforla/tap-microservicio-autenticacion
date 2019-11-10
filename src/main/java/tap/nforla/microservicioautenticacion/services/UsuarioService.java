package tap.nforla.microservicioautenticacion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tap.nforla.microservicioautenticacion.model.Usuario;
import tap.nforla.microservicioautenticacion.repo.UsuarioRepository;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

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

    public int getCuotaMaximaRequestsPorHora(String username) {

        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);

        return usuarioOptional.get().getMaxRequestsPorHora();
    }

}