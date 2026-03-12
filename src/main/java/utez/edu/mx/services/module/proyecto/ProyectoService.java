package utez.edu.mx.services.module.proyecto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    public ProyectoService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        List<Proyecto> proyectos = proyectoRepository.findAll();
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", proyectos, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<Proyecto> proyecto = proyectoRepository.findById(id);
        if (proyecto.isEmpty())
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Proyecto no encontrado", HttpStatus.BAD_REQUEST));
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", proyecto.get(), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByEquipo(Long idEquipo) {
        List<Proyecto> proyectos = proyectoRepository.findByEquipoIdEquipo(idEquipo);
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", proyectos, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByLider(Long idUsuario) {
        List<Proyecto> proyectos = proyectoRepository.findByLiderIdUsuario(idUsuario);
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", proyectos, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> save(Proyecto proyecto) {
        // Nombre obligatorio y único
        if (proyecto.getNombre() == null || proyecto.getNombre().isBlank())
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El nombre del proyecto es obligatorio", HttpStatus.BAD_REQUEST));

        if (proyectoRepository.existsByNombre(proyecto.getNombre()))
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El nombre del proyecto ya está en uso", HttpStatus.BAD_REQUEST));

        // Presupuesto mayor a 0
        if (proyecto.getPresupuestoTotal() != null &&
                proyecto.getPresupuestoTotal().compareTo(BigDecimal.ZERO) <= 0)
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El presupuesto debe ser mayor a 0", HttpStatus.BAD_REQUEST));

        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setEstado("PENDIENTE");
        Proyecto saved = proyectoRepository.save(proyecto);
        return ResponseEntity.ok(new AppiResponse("Proyecto registrado exitosamente", saved, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> update(Long id, Proyecto proyecto) {
        Optional<Proyecto> existing = proyectoRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Proyecto no encontrado", HttpStatus.BAD_REQUEST));

        // Validar nombre único al actualizar (ignorar el mismo proyecto)
        if (proyecto.getNombre() == null || proyecto.getNombre().isBlank())
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El nombre del proyecto es obligatorio", HttpStatus.BAD_REQUEST));

        boolean nombreDuplicado = proyectoRepository.existsByNombreAndIdProyectoNot(
                proyecto.getNombre(), id);
        if (nombreDuplicado)
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El nombre del proyecto ya está en uso", HttpStatus.BAD_REQUEST));

        // Presupuesto mayor a 0
        if (proyecto.getPresupuestoTotal() != null &&
                proyecto.getPresupuestoTotal().compareTo(BigDecimal.ZERO) <= 0)
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El presupuesto debe ser mayor a 0", HttpStatus.BAD_REQUEST));

        Proyecto p = existing.get();
        p.setNombre(proyecto.getNombre());
        p.setDescripcion(proyecto.getDescripcion());
        p.setObjetivo(proyecto.getObjetivo());
        p.setFechaFin(proyecto.getFechaFin());
        p.setEstado(proyecto.getEstado());
        p.setPresupuestoTotal(proyecto.getPresupuestoTotal());
        p.setLogo(proyecto.getLogo());
        p.setEquipo(proyecto.getEquipo());
        p.setLider(proyecto.getLider());
        return ResponseEntity.ok(new AppiResponse("Proyecto actualizado exitosamente", proyectoRepository.save(p), HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> cambiarEstado(Long id, String estado) {
        Optional<Proyecto> existing = proyectoRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Proyecto no encontrado", HttpStatus.BAD_REQUEST));
        Proyecto p = existing.get();
        p.setEstado(estado);
        proyectoRepository.save(p);
        return ResponseEntity.ok(new AppiResponse("Estado actualizado exitosamente", HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> delete(Long id) {
        Optional<Proyecto> existing = proyectoRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Proyecto no encontrado", HttpStatus.BAD_REQUEST));
        Proyecto p = existing.get();
        p.setEstado("CANCELADO");
        proyectoRepository.save(p);
        return ResponseEntity.ok(new AppiResponse("Proyecto cancelado exitosamente", HttpStatus.OK));
    }
}