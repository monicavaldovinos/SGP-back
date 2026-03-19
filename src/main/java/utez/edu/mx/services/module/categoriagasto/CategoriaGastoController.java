package utez.edu.mx.services.module.categoriagasto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;

@RestController
@RequestMapping("/sgp-api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaGastoController {

    private final CategoriaGastoService categoriaGastoService;

    public CategoriaGastoController(CategoriaGastoService categoriaGastoService) {
        this.categoriaGastoService = categoriaGastoService;
    }

    @GetMapping
    public ResponseEntity<AppiResponse> findAll() {
        return categoriaGastoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppiResponse> findById(@PathVariable Long id) {
        return categoriaGastoService.findById(id);
    }

    @PostMapping
    public ResponseEntity<AppiResponse> save(@RequestBody CategoriaGasto categoria) {
        return categoriaGastoService.save(categoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppiResponse> update(@PathVariable Long id, @RequestBody CategoriaGasto categoria) {
        return categoriaGastoService.update(id, categoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppiResponse> delete(@PathVariable Long id) {
        return categoriaGastoService.delete(id);
    }
}