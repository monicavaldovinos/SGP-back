package utez.edu.mx.services.module.pago;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByProyectoIdProyecto(Long idProyecto);
    List<Pago> findByUsuarioIdUsuario(Long idUsuario);
    List<Pago> findByEstatus(String estatus);
    List<Pago> findByProyectoIdProyectoAndEstatus(Long idProyecto, String estatus);
}