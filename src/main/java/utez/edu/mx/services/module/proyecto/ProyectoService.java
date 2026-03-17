package utez.edu.mx.services.module.proyecto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;
import utez.edu.mx.services.module.equipousuario.EquipoUsuario;
import utez.edu.mx.services.module.equipousuario.EquipoUsuarioRepository;
import utez.edu.mx.services.module.proyecto.dto.ProyectoDTO;
import utez.edu.mx.services.module.tarea.Tarea;
import utez.edu.mx.services.module.tarea.TareaRepository;
import utez.edu.mx.services.module.usuario.Usuario;
import utez.edu.mx.services.module.usuario.UsuarioRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EquipoUsuarioRepository equipoUsuarioRepository;

    public ProyectoService(
            ProyectoRepository proyectoRepository,
            TareaRepository tareaRepository,
            UsuarioRepository usuarioRepository,
            EquipoUsuarioRepository equipoUsuarioRepository
    ) {
        this.proyectoRepository = proyectoRepository;
        this.tareaRepository = tareaRepository;
        this.usuarioRepository = usuarioRepository;
        this.equipoUsuarioRepository = equipoUsuarioRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        List<ProyectoDTO> proyectos = proyectoRepository.findAll()
                .stream()
                .map(ProyectoDTO::new)
                .toList();

        return ResponseEntity.ok(new AppiResponse("Operación exitosa", proyectos, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<Proyecto> proyecto = proyectoRepository.findById(id);

        if (proyecto.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Proyecto no encontrado", HttpStatus.BAD_REQUEST));
        }

        return ResponseEntity.ok(
                new AppiResponse("Operación exitosa", new ProyectoDTO(proyecto.get()), HttpStatus.OK)
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findMisProyectos(String username) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppiResponse("Usuario no encontrado", HttpStatus.NOT_FOUND));
        }

        Usuario usuario = usuarioOpt.get();
        String rol = usuario.getRol() != null ? usuario.getRol().getNombre().toUpperCase() : "";

        List<Proyecto> proyectos;

        if ("SUPERADMIN".equals(rol)) {
            proyectos = proyectoRepository.findAll();
        } else if ("LIDER".equals(rol)) {
            proyectos = proyectoRepository.findByLiderIdUsuarioAndEstadoNotIgnoreCase(
                    usuario.getIdUsuario(),
                    "CANCELADO"
            );
        } else {
            List<EquipoUsuario> membresias = equipoUsuarioRepository.findByUsuarioIdUsuario(usuario.getIdUsuario());

            Set<Long> idsEquipo = membresias.stream()
                    .map(eu -> eu.getEquipo().getIdEquipo())
                    .collect(Collectors.toSet());

            proyectos = idsEquipo.stream()
                    .flatMap(idEquipo ->
                            proyectoRepository.findByEquipoIdEquipoAndEstadoNotIgnoreCase(idEquipo, "CANCELADO").stream()
                    )
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(
                                    Proyecto::getIdProyecto,
                                    p -> p,
                                    (a, b) -> a,
                                    LinkedHashMap::new
                            ),
                            m -> new ArrayList<>(m.values())
                    ));
        }

        List<ProyectoDTO> data = proyectos.stream()
                .map(ProyectoDTO::new)
                .toList();

        return ResponseEntity.ok(new AppiResponse("Operación exitosa", data, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByEquipo(Long idEquipo) {
        List<ProyectoDTO> proyectos = proyectoRepository.findByEquipoIdEquipo(idEquipo)
                .stream()
                .map(ProyectoDTO::new)
                .toList();

        return ResponseEntity.ok(new AppiResponse("Operación exitosa", proyectos, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByLider(Long idUsuario) {
        List<ProyectoDTO> proyectos = proyectoRepository.findByLiderIdUsuario(idUsuario)
                .stream()
                .map(ProyectoDTO::new)
                .toList();

        return ResponseEntity.ok(new AppiResponse("Operación exitosa", proyectos, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> save(Proyecto proyecto) {
        if (proyecto.getNombre() == null || proyecto.getNombre().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El nombre del proyecto es obligatorio", HttpStatus.BAD_REQUEST));
        }

        if (proyectoRepository.existsByNombre(proyecto.getNombre())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El nombre del proyecto ya está en uso", HttpStatus.BAD_REQUEST));
        }

        if (proyecto.getEquipo() == null || proyecto.getEquipo().getIdEquipo() == null) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Debes asignar un equipo al proyecto", HttpStatus.BAD_REQUEST));
        }

        boolean equipoConProyectoActivo = proyectoRepository
                .existsByEquipoIdEquipoAndEstadoNotIgnoreCase(proyecto.getEquipo().getIdEquipo(), "CANCELADO");

        if (equipoConProyectoActivo) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse(
                            "Ese equipo ya tiene un proyecto activo o pendiente asignado",
                            HttpStatus.BAD_REQUEST
                    ));
        }

        if (proyecto.getPresupuestoTotal() != null &&
                proyecto.getPresupuestoTotal().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El presupuesto debe ser mayor a 0", HttpStatus.BAD_REQUEST));
        }

        proyecto.setFechaInicio(LocalDate.now());

        if (proyecto.getEstado() == null || proyecto.getEstado().isBlank()) {
            proyecto.setEstado("PENDIENTE");
        }

        Proyecto saved = proyectoRepository.save(proyecto);

        return ResponseEntity.ok(
                new AppiResponse("Proyecto registrado exitosamente", new ProyectoDTO(saved), HttpStatus.OK)
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> update(Long id, Proyecto proyecto) {
        Optional<Proyecto> existing = proyectoRepository.findById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Proyecto no encontrado", HttpStatus.BAD_REQUEST));
        }

        if (proyecto.getNombre() == null || proyecto.getNombre().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El nombre del proyecto es obligatorio", HttpStatus.BAD_REQUEST));
        }

        boolean nombreDuplicado = proyectoRepository.existsByNombreAndIdProyectoNot(
                proyecto.getNombre(), id
        );

        if (nombreDuplicado) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El nombre del proyecto ya está en uso", HttpStatus.BAD_REQUEST));
        }

        if (proyecto.getEquipo() == null || proyecto.getEquipo().getIdEquipo() == null) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Debes asignar un equipo al proyecto", HttpStatus.BAD_REQUEST));
        }

        boolean equipoConOtroProyectoActivo = proyectoRepository
                .existsByEquipoIdEquipoAndEstadoNotIgnoreCaseAndIdProyectoNot(
                        proyecto.getEquipo().getIdEquipo(),
                        "CANCELADO",
                        id
                );

        if (equipoConOtroProyectoActivo) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse(
                            "Ese equipo ya tiene otro proyecto activo o pendiente asignado",
                            HttpStatus.BAD_REQUEST
                    ));
        }

        if (proyecto.getPresupuestoTotal() != null &&
                proyecto.getPresupuestoTotal().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El presupuesto debe ser mayor a 0", HttpStatus.BAD_REQUEST));
        }

        Proyecto p = existing.get();
        p.setNombre(proyecto.getNombre());
        p.setDescripcion(proyecto.getDescripcion());
        p.setObjetivo(proyecto.getObjetivo());
        p.setFechaFin(proyecto.getFechaFin());
        p.setEstado(proyecto.getEstado());
        p.setPresupuestoTotal(proyecto.getPresupuestoTotal());
        p.setLogo(proyecto.getLogo());
        p.setEquipo(proyecto.getEquipo());
        p.setLider(proyecto.getLider());

        return ResponseEntity.ok(
                new AppiResponse(
                        "Proyecto actualizado exitosamente",
                        new ProyectoDTO(proyectoRepository.save(p)),
                        HttpStatus.OK
                )
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> cambiarEstado(Long id, String estado) {
        Optional<Proyecto> existing = proyectoRepository.findById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Proyecto no encontrado", HttpStatus.BAD_REQUEST));
        }

        Proyecto p = existing.get();
        p.setEstado(estado);
        proyectoRepository.save(p);

        return ResponseEntity.ok(
                new AppiResponse("Estado actualizado exitosamente", HttpStatus.OK)
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> delete(Long id) {
        Optional<Proyecto> existing = proyectoRepository.findById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Proyecto no encontrado", HttpStatus.BAD_REQUEST));
        }

        Proyecto p = existing.get();
        p.setEstado("CANCELADO");
        proyectoRepository.save(p);

        return ResponseEntity.ok(
                new AppiResponse("Proyecto cancelado exitosamente", HttpStatus.OK)
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> getProgreso(Long idProyecto) {
        Optional<Proyecto> proyecto = proyectoRepository.findById(idProyecto);

        if (proyecto.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Proyecto no encontrado", HttpStatus.BAD_REQUEST));
        }

        List<Tarea> todasLasTareas = tareaRepository.findByProyectoIdProyecto(idProyecto);

        if (todasLasTareas.isEmpty()) {
            return ResponseEntity.ok(
                    new AppiResponse("Operación exitosa", 0, HttpStatus.OK)
            );
        }

        long completadas = todasLasTareas.stream()
                .filter(t -> "COMPLETADA".equalsIgnoreCase(t.getEstado()))
                .count();

        int progreso = (int) Math.round((completadas * 100.0) / todasLasTareas.size());

        return ResponseEntity.ok(
                new AppiResponse("Operación exitosa", progreso, HttpStatus.OK)
        );
    }
}