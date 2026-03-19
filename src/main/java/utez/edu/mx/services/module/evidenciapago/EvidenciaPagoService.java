package utez.edu.mx.services.module.evidenciapago;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EvidenciaPagoService {

    private final EvidenciaPagoRepository evidenciaPagoRepository;

    public EvidenciaPagoService(EvidenciaPagoRepository evidenciaPagoRepository) {
        this.evidenciaPagoRepository = evidenciaPagoRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", evidenciaPagoRepository.findAll(), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<EvidenciaPago> e = evidenciaPagoRepository.findById(id);
        if (e.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Evidencia no encontrada", HttpStatus.BAD_REQUEST));
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", e.get(), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByPago(Long idPago) {
        List<EvidenciaPago> evidencias = evidenciaPagoRepository.findByPagoIdPago(idPago);
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", evidencias, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> save(EvidenciaPago evidenciaPago) {
        evidenciaPago.setFechaSubida(LocalDate.now());
        EvidenciaPago saved = evidenciaPagoRepository.save(evidenciaPago);
        return ResponseEntity.ok(new AppiResponse("Evidencia registrada exitosamente", saved, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> delete(Long id) {
        Optional<EvidenciaPago> existing = evidenciaPagoRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Evidencia no encontrada", HttpStatus.BAD_REQUEST));
        evidenciaPagoRepository.deleteById(id);
        return ResponseEntity.ok(new AppiResponse("Evidencia eliminada exitosamente", HttpStatus.OK));
    }
}