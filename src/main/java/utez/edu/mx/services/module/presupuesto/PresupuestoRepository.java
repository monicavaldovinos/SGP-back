package utez.edu.mx.services.module.presupuesto;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {
    Optional<Presupuesto> findByProyectoIdProyecto(Long idProyecto);
    boolean existsByProyectoIdProyecto(Long idProyecto);
}