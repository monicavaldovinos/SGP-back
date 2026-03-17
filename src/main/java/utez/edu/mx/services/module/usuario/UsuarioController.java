package utez.edu.mx.services.module.usuario;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;

@RestController
@RequestMapping("/sgp-api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<AppiResponse> findAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppiResponse> findById(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    @GetMapping("/mi-perfil")
    public ResponseEntity<AppiResponse> miPerfil(Authentication authentication) {
        return usuarioService.miPerfil(authentication.getName());
    }

    @PostMapping
    public ResponseEntity<AppiResponse> save(@Valid @RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppiResponse> update(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        return usuarioService.update(id, usuario);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<AppiResponse> cambiarPassword(@PathVariable Long id, @RequestBody String newPassword) {
        return usuarioService.cambiarPassword(id, newPassword);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppiResponse> delete(@PathVariable Long id) {
        return usuarioService.delete(id);
    }

    @GetMapping("/disponibles-equipo")
    public ResponseEntity<AppiResponse> disponiblesParaEquipo() {
        return usuarioService.findDisponiblesParaEquipo();
    }
}