package utez.edu.mx.services.module.equipousuario;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;

@RestController
@RequestMapping("/sgp-api/equipo-usuario")
@CrossOrigin(origins = "*")
public class EquipoUsuarioController {

    private final EquipoUsuarioService equipoUsuarioService;

    public EquipoUsuarioController(EquipoUsuarioService equipoUsuarioService) {
        this.equipoUsuarioService = equipoUsuarioService;
    }

    @GetMapping
    public ResponseEntity<AppiResponse> findAll() {
        return equipoUsuarioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppiResponse> findById(@PathVariable Long id) {
        return equipoUsuarioService.findById(id);
    }

    @GetMapping("/equipo/{idEquipo}")
    public ResponseEntity<AppiResponse> findByEquipo(@PathVariable Long idEquipo) {
        return equipoUsuarioService.findByEquipo(idEquipo);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<AppiResponse> findByUsuario(@PathVariable Long idUsuario) {
        return equipoUsuarioService.findByUsuario(idUsuario);
    }

    @PostMapping
    public ResponseEntity<AppiResponse> save(@RequestBody EquipoUsuario equipoUsuario) {
        return equipoUsuarioService.save(equipoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppiResponse> update(@PathVariable Long id, @RequestBody EquipoUsuario equipoUsuario) {
        return equipoUsuarioService.update(id, equipoUsuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppiResponse> delete(@PathVariable Long id) {
        return equipoUsuarioService.delete(id);
    }
}