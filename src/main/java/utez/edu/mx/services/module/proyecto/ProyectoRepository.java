package utez.edu.mx.services.module.proyecto;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findByEstado(String estado);
    List<Proyecto> findByEquipoIdEquipo(Long idEquipo);
    List<Proyecto> findByLiderIdUsuario(Long idUsuario);
}