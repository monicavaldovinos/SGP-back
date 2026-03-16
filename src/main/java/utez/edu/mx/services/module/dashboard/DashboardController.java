package utez.edu.mx.services.module.dashboard;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;
import utez.edu.mx.services.module.usuario.Usuario;
import utez.edu.mx.services.module.usuario.UsuarioRepository;

import java.util.Optional;

@RestController
@RequestMapping("/sgp-api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;
    private final UsuarioRepository usuarioRepository;

    public DashboardController(DashboardService dashboardService, UsuarioRepository usuarioRepository) {
        this.dashboardService = dashboardService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/integrante/{username}")
    public ResponseEntity<AppiResponse> dashboardIntegrante(@PathVariable String username) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppiResponse("Usuario no encontrado", HttpStatus.NOT_FOUND));
        }

        DashboardResponseDTO dashboard = dashboardService.getDashboardIntegrante(usuarioOpt.get());

        return ResponseEntity.ok(
                new AppiResponse("Dashboard de integrante obtenido correctamente", dashboard, HttpStatus.OK)
        );
    }

    @GetMapping("/lider/{username}")
    public ResponseEntity<AppiResponse> dashboardLider(@PathVariable String username) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppiResponse("Usuario no encontrado", HttpStatus.NOT_FOUND));
        }

        LeaderDashboardDTO dashboard = dashboardService.getDashboardLider(usuarioOpt.get());

        return ResponseEntity.ok(
                new AppiResponse("Dashboard de líder obtenido correctamente", dashboard, HttpStatus.OK)
        );
    }

    @GetMapping("/admin")
    public ResponseEntity<AppiResponse> dashboardAdmin() {
        AdminDashboardDTO dashboard = dashboardService.getDashboardAdmin();

        return ResponseEntity.ok(
                new AppiResponse("Dashboard de superadmin obtenido correctamente", dashboard, HttpStatus.OK)
        );
    }
}