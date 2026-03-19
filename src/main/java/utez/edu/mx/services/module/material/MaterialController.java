package utez.edu.mx.services.module.material;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;

@RestController
@RequestMapping("/sgp-api/materiales")
@CrossOrigin(origins = "*")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping
    public ResponseEntity<AppiResponse> findAll() {
        return materialService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppiResponse> findById(@PathVariable Long id) {
        return materialService.findById(id);
    }

    @GetMapping("/proyecto/{idProyecto}")
    public ResponseEntity<AppiResponse> findByProyecto(@PathVariable Long idProyecto) {
        return materialService.findByProyecto(idProyecto);
    }

    @PostMapping
    public ResponseEntity<AppiResponse> save(@RequestBody Material material) {
        return materialService.save(material);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppiResponse> update(@PathVariable Long id, @RequestBody Material material) {
        return materialService.update(id, material);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppiResponse> delete(@PathVariable Long id) {
        return materialService.delete(id);
    }
}