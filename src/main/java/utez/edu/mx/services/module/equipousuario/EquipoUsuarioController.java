package utez.edu.mx.services.module.equipousuario;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;
import utez.edu.mx.services.module.equipousuario.dto.CambiarLiderRequestDTO;

@RestController
@RequestMapping("/sgp-api/equipo-usuario")
@CrossOrigin(origins = "*")
public class EquipoUsuarioController {

    private final EquipoUsuarioService equipoUsuarioService;

    public EquipoUsuarioController(EquipoUsuarioService equipoUsuarioService) {
        this.equipoUsuarioService = equipoUsuarioService;
    }

    @PostMapping("/asignar")
    public ResponseEntity<AppiResponse> asignarUsuarioAEquipo(
            @RequestParam Long idEquipo,
            @RequestParam Long idUsuario
    ) {
        return equipoUsuarioService.asignarUsuarioAEquipo(idEquipo, idUsuario);
    }

    @PostMapping("/cambiar-lider")
    public ResponseEntity<AppiResponse> cambiarLider(@Valid @RequestBody CambiarLiderRequestDTO dto) {
        return equipoUsuarioService.cambiarLider(dto);
    }

    @DeleteMapping("/quitar")
    public ResponseEntity<AppiResponse> quitarUsuarioDeEquipo(
            @RequestParam Long idEquipo,
            @RequestParam Long idUsuario
    ) {
        return equipoUsuarioService.quitarUsuarioDeEquipo(idEquipo, idUsuario);
    }
}