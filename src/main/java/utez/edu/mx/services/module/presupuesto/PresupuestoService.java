package utez.edu.mx.services.module.presupuesto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;

    public PresupuestoService(PresupuestoRepository presupuestoRepository) {
        this.presupuestoRepository = presupuestoRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", presupuestoRepository.findAll(), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<Presupuesto> p = presupuestoRepository.findById(id);
        if (p.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Presupuesto no encontrado", HttpStatus.BAD_REQUEST));
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", p.get(), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByProyecto(Long idProyecto) {
        Optional<Presupuesto> p = presupuestoRepository.findByProyectoIdProyecto(idProyecto);
        if (p.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Presupuesto no encontrado", HttpStatus.BAD_REQUEST));
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", p.get(), HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> save(Presupuesto presupuesto) {
        Long idProyecto = presupuesto.getProyecto().getIdProyecto();
        if (presupuestoRepository.existsByProyectoIdProyecto(idProyecto))
            return ResponseEntity.badRequest().body(new AppiResponse("Este proyecto ya tiene un presupuesto asignado", HttpStatus.BAD_REQUEST));

        presupuesto.setMontoUtilizado(BigDecimal.ZERO);
        presupuesto.setMontoDisponible(presupuesto.getMontoAsignado());
        presupuesto.setFechaRegistro(LocalDate.now());
        Presupuesto saved = presupuestoRepository.save(presupuesto);
        return ResponseEntity.ok(new AppiResponse("Presupuesto registrado exitosamente", saved, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> update(Long id, Presupuesto presupuesto) {
        Optional<Presupuesto> existing = presupuestoRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Presupuesto no encontrado", HttpStatus.BAD_REQUEST));

        Presupuesto p = existing.get();
        p.setMontoAsignado(presupuesto.getMontoAsignado());
        p.setMontoUtilizado(presupuesto.getMontoUtilizado());
        p.setMontoDisponible(presupuesto.getMontoAsignado().subtract(presupuesto.getMontoUtilizado()));
        Presupuesto updated = presupuestoRepository.save(p);
        return ResponseEntity.ok(new AppiResponse("Presupuesto actualizado exitosamente", updated, HttpStatus.OK));
    }
}