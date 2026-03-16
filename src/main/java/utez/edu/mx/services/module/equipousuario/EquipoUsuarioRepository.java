package utez.edu.mx.services.module.equipousuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoUsuarioRepository extends JpaRepository<EquipoUsuario, Long> {

    List<EquipoUsuario> findByUsuarioIdUsuario(Long idUsuario);

    List<EquipoUsuario> findByEquipoIdEquipo(Long idEquipo);

    Optional<EquipoUsuario> findByEquipoIdEquipoAndUsuarioIdUsuario(Long idEquipo, Long idUsuario);

    boolean existsByEquipoIdEquipoAndUsuarioIdUsuario(Long idEquipo, Long idUsuario);

    boolean existsByUsuarioIdUsuario(Long idUsuario);
}