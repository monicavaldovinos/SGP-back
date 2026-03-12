package utez.edu.mx.services.module.proyecto;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;

@RestController
@RequestMapping("/sgp-api/proyectos")
@CrossOrigin(origins = "*")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping
    public ResponseEntity<AppiResponse> findAll() {
        return proyectoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppiResponse> findById(@PathVariable Long id) {
        return proyectoService.findById(id);
    }

    @GetMapping("/equipo/{idEquipo}")
    public ResponseEntity<AppiResponse> findByEquipo(@PathVariable Long idEquipo) {
        return proyectoService.findByEquipo(idEquipo);
    }

    @GetMapping("/lider/{idUsuario}")
    public ResponseEntity<AppiResponse> findByLider(@PathVariable Long idUsuario) {
        return proyectoService.findByLider(idUsuario);
    }

    @PostMapping
    public ResponseEntity<AppiResponse> save(@Valid @RequestBody Proyecto proyecto) {
        return proyectoService.save(proyecto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppiResponse> update(@Valid @PathVariable Long id, @RequestBody Proyecto proyecto) {
        return proyectoService.update(id, proyecto);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<AppiResponse> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        return proyectoService.cambiarEstado(id, estado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppiResponse> delete(@PathVariable Long id) {
        return proyectoService.delete(id);
    }
}