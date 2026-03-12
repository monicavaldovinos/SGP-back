package utez.edu.mx.services.module.equipo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;

@RestController
@RequestMapping("/sgp-api/equipos")
@CrossOrigin(origins = "*")
public class EquipoController {

    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    @GetMapping
    public ResponseEntity<AppiResponse> findAll() {
        return equipoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppiResponse> findById(@PathVariable Long id) {
        return equipoService.findById(id);
    }

    @PostMapping
    public ResponseEntity<AppiResponse> save(@RequestBody Equipo equipo) {
        return equipoService.save(equipo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppiResponse> update(@PathVariable Long id, @RequestBody Equipo equipo) {
        return equipoService.update(id, equipo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppiResponse> delete(@PathVariable Long id) {
        return equipoService.delete(id);
    }
}