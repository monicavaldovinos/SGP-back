package utez.edu.mx.services.module.pago;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByProyectoIdProyecto(Long idProyecto);

    List<Pago> findByUsuarioIdUsuario(Long idUsuario);

    List<Pago> findByProyectoIdProyectoAndUsuarioIdUsuario(Long idProyecto, Long idUsuario);

    List<Pago> findByProyectoIdProyectoAndPeriodo(Long idProyecto, String periodo);

    boolean existsByProyectoIdProyectoAndUsuarioIdUsuarioAndPeriodo(Long idProyecto, Long idUsuario, String periodo);
}