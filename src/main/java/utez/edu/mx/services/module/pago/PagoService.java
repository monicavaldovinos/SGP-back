package utez.edu.mx.services.module.pago;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", pagoRepository.findAll(), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<Pago> p = pagoRepository.findById(id);
        if (p.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Pago no encontrado", HttpStatus.BAD_REQUEST));
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", p.get(), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByProyecto(Long idProyecto) {
        List<Pago> pagos = pagoRepository.findByProyectoIdProyecto(idProyecto);
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", pagos, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByUsuario(Long idUsuario) {
        List<Pago> pagos = pagoRepository.findByUsuarioIdUsuario(idUsuario);
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", pagos, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> save(Pago pago) {
        pago.setFechaPago(LocalDate.now());
        pago.setEstatus("PENDIENTE");
        Pago saved = pagoRepository.save(pago);
        return ResponseEntity.ok(new AppiResponse("Pago registrado exitosamente", saved, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> update(Long id, Pago pago) {
        Optional<Pago> existing = pagoRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Pago no encontrado", HttpStatus.BAD_REQUEST));

        Pago p = existing.get();
        p.setConcepto(pago.getConcepto());
        p.setDescripcion(pago.getDescripcion());
        p.setMonto(pago.getMonto());
        p.setHoras(pago.getHoras());
        p.setTarifa(pago.getTarifa());
        p.setFormaPago(pago.getFormaPago());
        p.setPeriodo(pago.getPeriodo());
        p.setCategoria(pago.getCategoria());
        return ResponseEntity.ok(new AppiResponse("Pago actualizado exitosamente", pagoRepository.save(p), HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> cambiarEstatus(Long id, String estatus) {
        Optional<Pago> existing = pagoRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Pago no encontrado", HttpStatus.BAD_REQUEST));
        Pago p = existing.get();
        p.setEstatus(estatus);
        pagoRepository.save(p);
        return ResponseEntity.ok(new AppiResponse("Estatus actualizado exitosamente", HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> delete(Long id) {
        Optional<Pago> existing = pagoRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Pago no encontrado", HttpStatus.BAD_REQUEST));
        pagoRepository.deleteById(id);
        return ResponseEntity.ok(new AppiResponse("Pago eliminado exitosamente", HttpStatus.OK));
    }
}