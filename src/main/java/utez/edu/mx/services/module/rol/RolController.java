package utez.edu.mx.services.module.rol;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;

@RestController
@RequestMapping("/sgp-api/roles")
@CrossOrigin(origins = "*")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping("")
    public ResponseEntity<AppiResponse> findAll() {
        return rolService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppiResponse> findById(@PathVariable Long id) {
        return rolService.findById(id);
    }

    @PostMapping("")
    public ResponseEntity<AppiResponse> save(@RequestBody Rol rol) {
        return rolService.save(rol);
    }

    @PutMapping("")
    public ResponseEntity<AppiResponse> update(@RequestBody Rol rol) {
        return rolService.update(rol);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppiResponse> delete(@PathVariable Long id) {
        return rolService.delete(id);
    }
}