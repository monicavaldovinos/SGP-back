package utez.edu.mx.services.module.tarea;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByProyectoIdProyecto(Long idProyecto);

    List<Tarea> findByUsuarioAsignadoIdUsuario(Long idUsuario);

    List<Tarea> findByProyectoIdProyectoAndEstado(Long idProyecto, String estado);
}