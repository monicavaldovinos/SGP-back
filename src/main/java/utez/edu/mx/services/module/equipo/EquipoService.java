package utez.edu.mx.services.module.equipo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EquipoService {

    private final EquipoRepository equipoRepository;

    public EquipoService(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    // GET ALL ACTIVOS
    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        List<Equipo> equipos = equipoRepository.findByEstatus("ACTIVO");
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", equipos, HttpStatus.OK));
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<Equipo> equipo = equipoRepository.findById(id);
        if (equipo.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Equipo no encontrado", HttpStatus.BAD_REQUEST));
        }
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", equipo.get(), HttpStatus.OK));
    }

    // SAVE
    @Transactional
    public ResponseEntity<AppiResponse> save(Equipo equipo) {
        if (equipoRepository.existsByNombreEquipo(equipo.getNombreEquipo())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Ya existe un equipo con ese nombre", HttpStatus.BAD_REQUEST));
        }
        equipo.setFechaCreacion(LocalDate.now());
        equipo.setEstatus("ACTIVO");
        Equipo saved = equipoRepository.save(equipo);
        return ResponseEntity.ok(new AppiResponse("Equipo registrado exitosamente", saved, HttpStatus.OK));
    }

    // UPDATE
    @Transactional
    public ResponseEntity<AppiResponse> update(Long id, Equipo equipo) {
        Optional<Equipo> existing = equipoRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Equipo no encontrado", HttpStatus.BAD_REQUEST));
        }
        Equipo e = existing.get();
        e.setNombreEquipo(equipo.getNombreEquipo());
        e.setDescripcion(equipo.getDescripcion());
        e.setLogo(equipo.getLogo());
        Equipo updated = equipoRepository.save(e);
        return ResponseEntity.ok(new AppiResponse("Equipo actualizado exitosamente", updated, HttpStatus.OK));
    }

    // DELETE LÓGICO
    @Transactional
    public ResponseEntity<AppiResponse> delete(Long id) {
        Optional<Equipo> existing = equipoRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Equipo no encontrado", HttpStatus.BAD_REQUEST));
        }
        Equipo e = existing.get();
        e.setEstatus("INACTIVO");
        equipoRepository.save(e);
        return ResponseEntity.ok(new AppiResponse("Equipo eliminado exitosamente", HttpStatus.OK));
    }
}