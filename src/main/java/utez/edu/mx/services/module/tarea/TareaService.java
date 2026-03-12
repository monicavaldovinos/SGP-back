package utez.edu.mx.services.module.tarea;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", tareaRepository.findAll(), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<Tarea> t = tareaRepository.findById(id);
        if (t.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Tarea no encontrada", HttpStatus.BAD_REQUEST));
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", t.get(), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByProyecto(Long idProyecto) {
        List<Tarea> tareas = tareaRepository.findByProyectoIdProyecto(idProyecto);
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", tareas, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByUsuario(Long idUsuario) {
        List<Tarea> tareas = tareaRepository.findByUsuarioAsignadoIdUsuario(idUsuario);
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", tareas, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByProyectoAndEstado(Long idProyecto, String estado) {
        List<Tarea> tareas = tareaRepository.findByProyectoIdProyectoAndEstado(idProyecto, estado);
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", tareas, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> save(Tarea tarea) {
        tarea.setFechaInicio(LocalDate.now());
        tarea.setEstado("PENDIENTE");
        Tarea saved = tareaRepository.save(tarea);
        return ResponseEntity.ok(new AppiResponse("Tarea registrada exitosamente", saved, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> update(Long id, Tarea tarea) {
        Optional<Tarea> existing = tareaRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Tarea no encontrada", HttpStatus.BAD_REQUEST));

        Tarea t = existing.get();
        t.setNombre(tarea.getNombre());
        t.setDescripcion(tarea.getDescripcion());
        t.setFechaFin(tarea.getFechaFin());
        t.setPrioridad(tarea.getPrioridad());
        t.setUsuarioAsignado(tarea.getUsuarioAsignado());
        Tarea updated = tareaRepository.save(t);
        return ResponseEntity.ok(new AppiResponse("Tarea actualizada exitosamente", updated, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> cambiarEstado(Long id, String estado) {
        Optional<Tarea> existing = tareaRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Tarea no encontrada", HttpStatus.BAD_REQUEST));

        Tarea t = existing.get();
        t.setEstado(estado);
        tareaRepository.save(t);
        return ResponseEntity.ok(new AppiResponse("Estado actualizado exitosamente", HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> delete(Long id) {
        Optional<Tarea> existing = tareaRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Tarea no encontrada", HttpStatus.BAD_REQUEST));

        tareaRepository.deleteById(id);
        return ResponseEntity.ok(new AppiResponse("Tarea eliminada exitosamente", HttpStatus.OK));
    }
}