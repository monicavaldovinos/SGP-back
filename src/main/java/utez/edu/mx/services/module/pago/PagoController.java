package utez.edu.mx.services.module.pago;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;
import utez.edu.mx.services.module.pago.dto.GenerarPagosPeriodoDTO;
import utez.edu.mx.services.module.pago.dto.RealizarPagoDTO;

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

    @GetMapping("/mis-pagos")
    public ResponseEntity<AppiResponse> misPagos(Authentication authentication) {
        return pagoService.findMisPagos(authentication.getName());
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
    public ResponseEntity<AppiResponse> save(@Valid @RequestBody Pago pago) {
        return pagoService.save(pago);
    }

    @PostMapping("/generar-periodo")
    public ResponseEntity<AppiResponse> generarPagosPeriodo(
            @RequestBody GenerarPagosPeriodoDTO dto,
            Authentication authentication
    ) {
        return pagoService.generarPagosPeriodo(dto, authentication.getName());
    }

    @PatchMapping("/{id}/realizar")
    public ResponseEntity<AppiResponse> realizarPago(
            @PathVariable Long id,
            @RequestBody RealizarPagoDTO dto,
            Authentication authentication
    ) {
        return pagoService.realizarPago(id, dto, authentication.getName());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppiResponse> update(@Valid @PathVariable Long id, @RequestBody Pago pago) {
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

    @GetMapping("/resumen/{idProyecto}")
    public ResponseEntity<AppiResponse> resumenPorProyecto(@PathVariable Long idProyecto) {
        return pagoService.resumenPorProyecto(idProyecto);
    }

}