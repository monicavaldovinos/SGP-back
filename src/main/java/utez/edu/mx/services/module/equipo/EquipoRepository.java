package utez.edu.mx.services.module.equipo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    List<Equipo> findByEstatus(String estatus);
    boolean existsByNombreEquipo(String nombreEquipo);
    boolean existsByNombreEquipoAndIdEquipoNot(String nombreEquipo, Long idEquipo);
}