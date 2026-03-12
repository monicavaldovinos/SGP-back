package utez.edu.mx.services.module.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByUsername(String username);
    boolean existsByCorreo(String correo);
    boolean existsByUsername(String username);
}