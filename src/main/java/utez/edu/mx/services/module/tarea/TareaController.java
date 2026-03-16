package utez.edu.mx.services.module.tarea;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;

@RestController
@RequestMapping("/sgp-api/tareas")
@CrossOrigin(origins = "*")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @GetMapping
    public ResponseEntity<AppiResponse> findAll() {
        return tareaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppiResponse> findById(@PathVariable Long id) {
        return tareaService.findById(id);
    }

    @GetMapping("/mis-tareas")
    public ResponseEntity<AppiResponse> misTareas(Authentication authentication) {
        return tareaService.findMisTareas(authentication.getName());
    }

    @GetMapping("/proyecto/{idProyecto}")
    public ResponseEntity<AppiResponse> findByProyecto(@PathVariable Long idProyecto) {
        return tareaService.findByProyecto(idProyecto);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<AppiResponse> findByUsuario(@PathVariable Long idUsuario) {
        return tareaService.findByUsuario(idUsuario);
    }

    @GetMapping("/proyecto/{idProyecto}/estado")
    public ResponseEntity<AppiResponse> findByProyectoAndEstado(
            @PathVariable Long idProyecto, @RequestParam String estado) {
        return tareaService.findByProyectoAndEstado(idProyecto, estado);
    }

    @PostMapping
    public ResponseEntity<AppiResponse> save(@Valid @RequestBody Tarea tarea) {
        return tareaService.save(tarea);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppiResponse> update(@Valid @PathVariable Long id, @RequestBody Tarea tarea) {
        return tareaService.update(id, tarea);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<AppiResponse> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        return tareaService.cambiarEstado(id, estado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppiResponse> delete(@PathVariable Long id) {
        return tareaService.delete(id);
    }
}