package utez.edu.mx.services.module.proyecto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdProyectoNot(String nombre, Long idProyecto);

    List<Proyecto> findByEquipoIdEquipo(Long idEquipo);

    List<Proyecto> findByLiderIdUsuario(Long idUsuario);

    List<Proyecto> findByEquipoIdEquipoAndEstadoNotIgnoreCase(Long idEquipo, String estado);

    List<Proyecto> findByLiderIdUsuarioAndEstadoNotIgnoreCase(Long idUsuario, String estado);

    boolean existsByEquipoIdEquipoAndEstadoNotIgnoreCase(Long idEquipo, String estado);

    boolean existsByEquipoIdEquipoAndEstadoNotIgnoreCaseAndIdProyectoNot(Long idEquipo, String estado, Long idProyecto);
}