package utez.edu.mx.services.module.tarea;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;
import utez.edu.mx.services.module.tarea.TareaDTO;
import utez.edu.mx.services.module.usuario.Usuario;
import utez.edu.mx.services.module.usuario.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;

    public TareaService(TareaRepository tareaRepository, UsuarioRepository usuarioRepository) {
        this.tareaRepository = tareaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        List<TareaDTO> tareas = tareaRepository.findAll()
                .stream().map(TareaDTO::new).toList();
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", tareas, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<Tarea> t = tareaRepository.findById(id);
        if (t.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Tarea no encontrada", HttpStatus.BAD_REQUEST));
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", new TareaDTO(t.get()), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findMisTareas(String username) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppiResponse("Usuario no encontrado", HttpStatus.NOT_FOUND));
        }

        Usuario usuario = usuarioOpt.get();
        String rol = usuario.getRol() != null ? usuario.getRol().getNombre().toUpperCase() : "";

        List<TareaDTO> data = ("SUPERADMIN".equals(rol)
                ? tareaRepository.findAll().stream()
                : tareaRepository.findByUsuarioAsignadoIdUsuario(usuario.getIdUsuario()).stream())
                .map(TareaDTO::new)
                .toList();

        return ResponseEntity.ok(new AppiResponse("Operación exitosa", data, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByProyecto(Long idProyecto) {
        List<TareaDTO> tareas = tareaRepository.findByProyectoIdProyecto(idProyecto)
                .stream().map(TareaDTO::new).toList();
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", tareas, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByUsuario(Long idUsuario) {
        List<TareaDTO> tareas = tareaRepository.findByUsuarioAsignadoIdUsuario(idUsuario)
                .stream().map(TareaDTO::new).toList();
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", tareas, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByProyectoAndEstado(Long idProyecto, String estado) {
        List<TareaDTO> tareas = tareaRepository.findByProyectoIdProyectoAndEstado(idProyecto, estado)
                .stream().map(TareaDTO::new).toList();
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", tareas, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> save(Tarea tarea) {
        tarea.setFechaInicio(LocalDate.now());
        tarea.setEstado("PENDIENTE");
        Tarea saved = tareaRepository.save(tarea);
        return ResponseEntity.ok(new AppiResponse("Tarea registrada exitosamente", new TareaDTO(saved), HttpStatus.OK));
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
        return ResponseEntity.ok(new AppiResponse("Tarea actualizada exitosamente", new TareaDTO(tareaRepository.save(t)), HttpStatus.OK));
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