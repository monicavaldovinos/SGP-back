package utez.edu.mx.services.module.pago;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;
import utez.edu.mx.services.module.pago.dto.PagoDTO;
import utez.edu.mx.services.module.presupuesto.Presupuesto;
import utez.edu.mx.services.module.presupuesto.PresupuestoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PresupuestoRepository presupuestoRepository;

    public PagoService(PagoRepository pagoRepository, PresupuestoRepository presupuestoRepository) {
        this.pagoRepository = pagoRepository;
        this.presupuestoRepository = presupuestoRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        List<PagoDTO> pagos = pagoRepository.findAll()
                .stream().map(PagoDTO::new).toList();
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", pagos, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<Pago> p = pagoRepository.findById(id);
        if (p.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Pago no encontrado", HttpStatus.BAD_REQUEST));
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", new PagoDTO(p.get()), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByProyecto(Long idProyecto) {
        List<PagoDTO> pagos = pagoRepository.findByProyectoIdProyecto(idProyecto)
                .stream().map(PagoDTO::new).toList();
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", pagos, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByUsuario(Long idUsuario) {
        List<PagoDTO> pagos = pagoRepository.findByUsuarioIdUsuario(idUsuario)
                .stream().map(PagoDTO::new).toList();
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", pagos, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> save(Pago pago) {
        Long idProyecto = pago.getProyecto().getIdProyecto();

        // Verificar que el proyecto tenga presupuesto asignado
        Optional<Presupuesto> presupuestoOpt = presupuestoRepository.findByProyectoIdProyecto(idProyecto);
        if (presupuestoOpt.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse(
                    "El proyecto no tiene presupuesto asignado", HttpStatus.BAD_REQUEST));

        Presupuesto presupuesto = presupuestoOpt.get();
        BigDecimal disponible = presupuesto.getMontoDisponible();

        // Verificar fondos suficientes
        if (disponible.compareTo(BigDecimal.ZERO) <= 0)
            return ResponseEntity.badRequest().body(new AppiResponse(
                    "Fondos insuficientes para realizar el pago", HttpStatus.BAD_REQUEST));

        if (pago.getMonto().compareTo(disponible) > 0)
            return ResponseEntity.badRequest().body(new AppiResponse(
                    "No hay suficiente presupuesto para realizar este pago. Disponible: $" + disponible,
                    HttpStatus.BAD_REQUEST));

        // Verificar periodo duplicado para el mismo usuario y proyecto
        if (pago.getPeriodo() != null && pago.getUsuario() != null) {
            boolean periodoDuplicado = pagoRepository
                    .findByProyectoIdProyectoAndUsuarioIdUsuario(idProyecto, pago.getUsuario().getIdUsuario())
                    .stream()
                    .anyMatch(p -> pago.getPeriodo().equals(p.getPeriodo()));
            if (periodoDuplicado)
                return ResponseEntity.badRequest().body(new AppiResponse(
                        "Este periodo ya fue pagado para este usuario en este proyecto", HttpStatus.BAD_REQUEST));
        }

        // Descontar del presupuesto
        presupuesto.setMontoUtilizado(presupuesto.getMontoUtilizado().add(pago.getMonto()));
        presupuesto.setMontoDisponible(disponible.subtract(pago.getMonto()));
        presupuestoRepository.save(presupuesto);

        // Calcular alerta de presupuesto
        BigDecimal porcentajeDisponible = presupuesto.getMontoDisponible()
                .divide(presupuesto.getMontoAsignado(), 2, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        String alerta = null;
        if (porcentajeDisponible.compareTo(BigDecimal.ZERO) <= 0)
            alerta = "FONDOS_INSUFICIENTES";
        else if (porcentajeDisponible.compareTo(BigDecimal.valueOf(10)) <= 0)
            alerta = "ALERTA_ROJA";
        else if (porcentajeDisponible.compareTo(BigDecimal.valueOf(20)) <= 0)
            alerta = "ALERTA_AMARILLA";

        pago.setFechaPago(LocalDate.now());
        pago.setEstatus("PENDIENTE");
        Pago saved = pagoRepository.save(pago);

        // Incluir alerta en la respuesta
        if (alerta != null) {
            java.util.Map<String, Object> data = new java.util.HashMap<>();
            data.put("pago", saved);
            data.put("alerta", alerta);
            data.put("montoDisponible", presupuesto.getMontoDisponible());
            return ResponseEntity.ok(new AppiResponse("Pago registrado exitosamente", data, HttpStatus.OK));
        }

        return ResponseEntity.ok(new AppiResponse("Pago registrado exitosamente", new PagoDTO(saved), HttpStatus.OK));
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