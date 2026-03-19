package utez.edu.mx.services.module.equipo;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;
import utez.edu.mx.services.module.equipo.dto.CreateTeamRequestDTO;

@RestController
@RequestMapping("/sgp-api/equipos")
@CrossOrigin(origins = "*")
public class EquipoController {

    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    @GetMapping
    public ResponseEntity<AppiResponse> findAll() {
        return equipoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppiResponse> findById(@PathVariable Long id) {
        return equipoService.findById(id);
    }

    @GetMapping("/mi-equipo")
    public ResponseEntity<AppiResponse> miEquipo(Authentication authentication) {
        return equipoService.findMiEquipo(authentication.getName());
    }

    @GetMapping("/{idEquipo}/integrantes")
    public ResponseEntity<AppiResponse> integrantesPorEquipo(@PathVariable Long idEquipo) {
        return equipoService.findIntegrantesByEquipo(idEquipo);
    }

    @GetMapping("/mis-integrantes")
    public ResponseEntity<AppiResponse> misIntegrantes(Authentication authentication) {
        return equipoService.findMisIntegrantes(authentication.getName());
    }

    @PostMapping
    public ResponseEntity<AppiResponse> save(@Valid @RequestBody Equipo equipo) {
        return equipoService.save(equipo);
    }

    @PostMapping("/crear-con-miembros")
    public ResponseEntity<AppiResponse> crearConMiembros(@Valid @RequestBody CreateTeamRequestDTO dto) {
        System.out.println(">>> SI ENTRO A crear-con-miembros");
        return equipoService.crearEquipoConMiembros(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppiResponse> update(@PathVariable Long id, @Valid @RequestBody Equipo equipo) {
        return equipoService.update(id, equipo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppiResponse> delete(@PathVariable Long id) {
        return equipoService.delete(id);
    }

    @GetMapping("/disponibles-proyecto")
    public ResponseEntity<AppiResponse> disponiblesParaProyecto() {
        return equipoService.findDisponiblesParaProyecto();
    }
}