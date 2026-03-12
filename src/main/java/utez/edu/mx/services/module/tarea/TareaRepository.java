package utez.edu.mx.services.module.tarea;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByProyectoIdProyecto(Long idProyecto);
    List<Tarea> findByUsuarioAsignadoIdUsuario(Long idUsuario);
    List<Tarea> findByEstado(String estado);
    List<Tarea> findByProyectoIdProyectoAndEstado(Long idProyecto, String estado);
}