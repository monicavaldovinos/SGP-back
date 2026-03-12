package utez.edu.mx.services.module.pago;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;

@RestController
@RequestMapping("/sgp-api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public ResponseEntity<AppiResponse> findAll() {
        return pagoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppiResponse> findById(@PathVariable Long id) {
        return pagoService.findById(id);
    }

    @GetMapping("/proyecto/{idProyecto}")
    public ResponseEntity<AppiResponse> findByProyecto(@PathVariable Long idProyecto) {
        return pagoService.findByProyecto(idProyecto);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<AppiResponse> findByUsuario(@PathVariable Long idUsuario) {
        return pagoService.findByUsuario(idUsuario);
    }

    @PostMapping
    public ResponseEntity<AppiResponse> save(@RequestBody Pago pago) {
        return pagoService.save(pago);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppiResponse> update(@PathVariable Long id, @RequestBody Pago pago) {
        return pagoService.update(id, pago);
    }

    @PatchMapping("/{id}/estatus")
    public ResponseEntity<AppiResponse> cambiarEstatus(@PathVariable Long id, @RequestParam String estatus) {
        return pagoService.cambiarEstatus(id, estatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppiResponse> delete(@PathVariable Long id) {
        return pagoService.delete(id);
    }
}