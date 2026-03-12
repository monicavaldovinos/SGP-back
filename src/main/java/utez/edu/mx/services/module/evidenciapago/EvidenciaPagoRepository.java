package utez.edu.mx.services.module.evidenciapago;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvidenciaPagoRepository extends JpaRepository<EvidenciaPago, Long> {
    List<EvidenciaPago> findByPagoIdPago(Long idPago);
}