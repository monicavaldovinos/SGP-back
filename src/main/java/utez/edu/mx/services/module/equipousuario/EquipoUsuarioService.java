package utez.edu.mx.services.module.equipousuario;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EquipoUsuarioService {

    private final EquipoUsuarioRepository equipoUsuarioRepository;

    public EquipoUsuarioService(EquipoUsuarioRepository equipoUsuarioRepository) {
        this.equipoUsuarioRepository = equipoUsuarioRepository;
    }

    // GET ALL
    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        List<EquipoUsuario> lista = equipoUsuarioRepository.findAll();
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", lista, HttpStatus.OK));
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<EquipoUsuario> eu = equipoUsuarioRepository.findById(id);
        if (eu.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Relación no encontrada", HttpStatus.BAD_REQUEST));
        }
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", eu.get(), HttpStatus.OK));
    }

    // GET BY EQUIPO
    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByEquipo(Long idEquipo) {
        List<EquipoUsuario> lista = equipoUsuarioRepository.findByEquipoIdEquipo(idEquipo);
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", lista, HttpStatus.OK));
    }

    // GET BY USUARIO
    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByUsuario(Long idUsuario) {
        List<EquipoUsuario> lista = equipoUsuarioRepository.findByUsuarioIdUsuario(idUsuario);
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", lista, HttpStatus.OK));
    }

    // SAVE
    @Transactional
    public ResponseEntity<AppiResponse> save(EquipoUsuario equipoUsuario) {
        Long idEquipo = equipoUsuario.getEquipo().getIdEquipo();
        Long idUsuario = equipoUsuario.getUsuario().getIdUsuario();

        if (equipoUsuarioRepository.existsByEquipoIdEquipoAndUsuarioIdUsuario(idEquipo, idUsuario)) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El usuario ya pertenece a este equipo", HttpStatus.BAD_REQUEST));
        }
        equipoUsuario.setFechaUnion(LocalDate.now());
        EquipoUsuario saved = equipoUsuarioRepository.save(equipoUsuario);
        return ResponseEntity.ok(new AppiResponse("Usuario agregado al equipo exitosamente", saved, HttpStatus.OK));
    }

    // UPDATE
    @Transactional
    public ResponseEntity<AppiResponse> update(Long id, EquipoUsuario equipoUsuario) {
        Optional<EquipoUsuario> existing = equipoUsuarioRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Relación no encontrada", HttpStatus.BAD_REQUEST));
        }
        EquipoUsuario eu = existing.get();
        eu.setRolEnEquipo(equipoUsuario.getRolEnEquipo());
        EquipoUsuario updated = equipoUsuarioRepository.save(eu);
        return ResponseEntity.ok(new AppiResponse("Relación actualizada exitosamente", updated, HttpStatus.OK));
    }

    // DELETE
    @Transactional
    public ResponseEntity<AppiResponse> delete(Long id) {
        Optional<EquipoUsuario> existing = equipoUsuarioRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Relación no encontrada", HttpStatus.BAD_REQUEST));
        }
        equipoUsuarioRepository.deleteById(id);
        return ResponseEntity.ok(new AppiResponse("Usuario removido del equipo exitosamente", HttpStatus.OK));
    }
}