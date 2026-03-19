package utez.edu.mx.services.module.presupuesto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;

@RestController
@RequestMapping("/sgp-api/presupuestos")
@CrossOrigin(origins = "*")
public class PresupuestoController {

    private final PresupuestoService presupuestoService;

    public PresupuestoController(PresupuestoService presupuestoService) {
        this.presupuestoService = presupuestoService;
    }

    @GetMapping
    public ResponseEntity<AppiResponse> findAll() {
        return presupuestoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppiResponse> findById(@PathVariable Long id) {
        return presupuestoService.findById(id);
    }

    @GetMapping("/proyecto/{idProyecto}")
    public ResponseEntity<AppiResponse> findByProyecto(@PathVariable Long idProyecto) {
        return presupuestoService.findByProyecto(idProyecto);
    }

    @PostMapping
    public ResponseEntity<AppiResponse> save(@RequestBody Presupuesto presupuesto) {
        return presupuestoService.save(presupuesto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppiResponse> update(@PathVariable Long id, @RequestBody Presupuesto presupuesto) {
        return presupuestoService.update(id, presupuesto);
    }
}