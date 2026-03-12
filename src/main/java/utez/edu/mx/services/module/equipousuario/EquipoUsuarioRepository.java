package utez.edu.mx.services.module.equipousuario;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EquipoUsuarioRepository extends JpaRepository<EquipoUsuario, Long> {
    List<EquipoUsuario> findByEquipoIdEquipo(Long idEquipo);
    List<EquipoUsuario> findByUsuarioIdUsuario(Long idUsuario);
    boolean existsByEquipoIdEquipoAndUsuarioIdUsuario(Long idEquipo, Long idUsuario);
}