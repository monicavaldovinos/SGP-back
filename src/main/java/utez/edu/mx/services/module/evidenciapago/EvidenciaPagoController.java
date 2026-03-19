package utez.edu.mx.services.module.evidenciapago;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;

@RestController
@RequestMapping("/sgp-api/evidencias")
@CrossOrigin(origins = "*")
public class EvidenciaPagoController {

    private final EvidenciaPagoService evidenciaPagoService;

    public EvidenciaPagoController(EvidenciaPagoService evidenciaPagoService) {
        this.evidenciaPagoService = evidenciaPagoService;
    }

    @GetMapping
    public ResponseEntity<AppiResponse> findAll() {
        return evidenciaPagoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppiResponse> findById(@PathVariable Long id) {
        return evidenciaPagoService.findById(id);
    }

    @GetMapping("/pago/{idPago}")
    public ResponseEntity<AppiResponse> findByPago(@PathVariable Long idPago) {
        return evidenciaPagoService.findByPago(idPago);
    }

    @PostMapping
    public ResponseEntity<AppiResponse> save(@RequestBody EvidenciaPago evidenciaPago) {
        return evidenciaPagoService.save(evidenciaPago);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppiResponse> delete(@PathVariable Long id) {
        return evidenciaPagoService.delete(id);
    }
}