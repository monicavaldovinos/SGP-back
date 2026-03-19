package utez.edu.mx.services.module.equipo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;
import utez.edu.mx.services.module.equipo.dto.CreateTeamRequestDTO;
import utez.edu.mx.services.module.equipousuario.EquipoUsuario;
import utez.edu.mx.services.module.equipousuario.EquipoUsuarioRepository;
import utez.edu.mx.services.module.proyecto.Proyecto;
import utez.edu.mx.services.module.proyecto.ProyectoRepository;
import utez.edu.mx.services.module.rol.Rol;
import utez.edu.mx.services.module.rol.RolRepository;
import utez.edu.mx.services.module.usuario.Usuario;
import utez.edu.mx.services.module.usuario.UsuarioRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EquipoService {

    private final EquipoRepository equipoRepository;
    private final EquipoUsuarioRepository equipoUsuarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ProyectoRepository proyectoRepository;

    public EquipoService(
            EquipoRepository equipoRepository,
            EquipoUsuarioRepository equipoUsuarioRepository,
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            ProyectoRepository proyectoRepository
    ) {
        this.equipoRepository = equipoRepository;
        this.equipoUsuarioRepository = equipoUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.proyectoRepository = proyectoRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        return ResponseEntity.ok(
                new AppiResponse("Operación exitosa", equipoRepository.findAll(), HttpStatus.OK)
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<Equipo> equipo = equipoRepository.findById(id);

        if (equipo.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Equipo no encontrado", HttpStatus.BAD_REQUEST));
        }

        return ResponseEntity.ok(
                new AppiResponse("Operación exitosa", equipo.get(), HttpStatus.OK)
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findMiEquipo(String username) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppiResponse("Usuario no encontrado", HttpStatus.NOT_FOUND));
        }

        List<EquipoUsuario> relaciones =
                equipoUsuarioRepository.findByUsuarioIdUsuario(usuarioOpt.get().getIdUsuario());

        List<Equipo> equipos = relaciones.stream()
                .map(EquipoUsuario::getEquipo)
                .toList();

        return ResponseEntity.ok(
                new AppiResponse("Operación exitosa", equipos, HttpStatus.OK)
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findIntegrantesByEquipo(Long idEquipo) {
        List<Usuario> integrantes = equipoUsuarioRepository.findByEquipoIdEquipo(idEquipo)
                .stream()
                .map(EquipoUsuario::getUsuario)
                .toList();

        return ResponseEntity.ok(
                new AppiResponse("Operación exitosa", integrantes, HttpStatus.OK)
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findMisIntegrantes(String username) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppiResponse("Usuario no encontrado", HttpStatus.NOT_FOUND));
        }

        List<EquipoUsuario> misRelaciones =
                equipoUsuarioRepository.findByUsuarioIdUsuario(usuarioOpt.get().getIdUsuario());

        if (misRelaciones.isEmpty()) {
            return ResponseEntity.ok(
                    new AppiResponse("Operación exitosa", List.of(), HttpStatus.OK)
            );
        }

        Long idEquipo = misRelaciones.get(0).getEquipo().getIdEquipo();

        List<Usuario> integrantes = equipoUsuarioRepository.findByEquipoIdEquipo(idEquipo)
                .stream()
                .map(EquipoUsuario::getUsuario)
                .toList();

        return ResponseEntity.ok(
                new AppiResponse("Operación exitosa", integrantes, HttpStatus.OK)
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> save(Equipo equipo) {
        if (equipoRepository.existsByNombreEquipo(equipo.getNombreEquipo())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Ya existe un equipo con ese nombre", HttpStatus.BAD_REQUEST));
        }

        equipo.setFechaCreacion(LocalDate.now());

        if (equipo.getEstatus() == null || equipo.getEstatus().isBlank()) {
            equipo.setEstatus("ACTIVO");
        }

        Equipo saved = equipoRepository.save(equipo);

        return ResponseEntity.ok(
                new AppiResponse("Equipo registrado correctamente", saved, HttpStatus.OK)
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> crearEquipoConMiembros(CreateTeamRequestDTO dto) {

        if (dto.getNombreEquipo() == null || dto.getNombreEquipo().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El nombre del equipo es obligatorio", HttpStatus.BAD_REQUEST));
        }

        if (dto.getIdProyecto() == null) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Debes seleccionar un proyecto", HttpStatus.BAD_REQUEST));
        }

        if (dto.getIdLider() == null) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Debes seleccionar un líder", HttpStatus.BAD_REQUEST));
        }

        if (dto.getIntegrantesIds() == null || dto.getIntegrantesIds().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Debes seleccionar integrantes", HttpStatus.BAD_REQUEST));
        }

        String nombreEquipo = dto.getNombreEquipo().trim();

        if (equipoRepository.existsByNombreEquipo(nombreEquipo)) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Ya existe un equipo con ese nombre", HttpStatus.BAD_REQUEST));
        }

        Set<Long> idsUnicos = new HashSet<>(dto.getIntegrantesIds());

        if (idsUnicos.size() != dto.getIntegrantesIds().size()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("No puedes repetir integrantes", HttpStatus.BAD_REQUEST));
        }

        if (idsUnicos.contains(dto.getIdLider())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El líder no debe ir también en integrantes", HttpStatus.BAD_REQUEST));
        }

        Optional<Rol> rolLiderOpt = rolRepository.findByNombreIgnoreCase("LIDER");
        Optional<Rol> rolIntegranteOpt = rolRepository.findByNombreIgnoreCase("INTEGRANTE");

        if (rolLiderOpt.isEmpty() || rolIntegranteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AppiResponse(
                            "No se encontraron los roles necesarios en la base de datos",
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }

        Optional<Usuario> liderOpt = usuarioRepository.findById(dto.getIdLider());
        if (liderOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El líder seleccionado no existe", HttpStatus.BAD_REQUEST));
        }

        Optional<Proyecto> proyectoOpt = proyectoRepository.findById(dto.getIdProyecto());
        if (proyectoOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El proyecto seleccionado no existe", HttpStatus.BAD_REQUEST));
        }

        Usuario lider = liderOpt.get();
        Proyecto proyecto = proyectoOpt.get();

        if (equipoUsuarioRepository.existsByUsuarioIdUsuario(lider.getIdUsuario())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse(
                            "El líder " + lider.getUsername() + " ya pertenece a un equipo",
                            HttpStatus.BAD_REQUEST
                    ));
        }

        for (Long idUsuario : idsUnicos) {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new AppiResponse("Uno de los integrantes seleccionados no existe", HttpStatus.BAD_REQUEST));
            }

            Usuario usuario = usuarioOpt.get();

            if (equipoUsuarioRepository.existsByUsuarioIdUsuario(usuario.getIdUsuario())) {
                return ResponseEntity.badRequest()
                        .body(new AppiResponse(
                                "El usuario " + usuario.getUsername() + " ya pertenece a un equipo",
                                HttpStatus.BAD_REQUEST
                        ));
            }
        }

        if (proyecto.getEquipo() != null) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El proyecto ya está asignado a un equipo", HttpStatus.BAD_REQUEST));
        }

        Equipo equipo = new Equipo();
        equipo.setNombreEquipo(nombreEquipo);
        equipo.setDescripcion(dto.getDescripcion());
        equipo.setLogo(dto.getLogo());
        equipo.setFechaCreacion(LocalDate.now());
        equipo.setEstatus("ACTIVO");

        Equipo equipoGuardado = equipoRepository.saveAndFlush(equipo);

        if (equipoGuardado.getIdEquipo() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AppiResponse("No se pudo generar el ID del equipo", HttpStatus.INTERNAL_SERVER_ERROR));
        }

        EquipoUsuario relacionLider = new EquipoUsuario();
        relacionLider.setEquipo(equipoGuardado);
        relacionLider.setUsuario(lider);
        relacionLider.setRolEnEquipo("LIDER");
        relacionLider.setFechaUnion(LocalDate.now());
        equipoUsuarioRepository.saveAndFlush(relacionLider);

        lider.setRol(rolLiderOpt.get());
        usuarioRepository.save(lider);

        for (Long idUsuario : idsUnicos) {
            Usuario usuario = usuarioRepository.findById(idUsuario).get();

            EquipoUsuario relacion = new EquipoUsuario();
            relacion.setEquipo(equipoGuardado);
            relacion.setUsuario(usuario);
            relacion.setRolEnEquipo("INTEGRANTE");
            relacion.setFechaUnion(LocalDate.now());
            equipoUsuarioRepository.save(relacion);

            usuario.setRol(rolIntegranteOpt.get());
            usuarioRepository.save(usuario);
        }

        proyecto.setEquipo(equipoGuardado);
        proyecto.setLider(lider);
        proyectoRepository.save(proyecto);

        return ResponseEntity.ok(
                new AppiResponse(
                        "Equipo creado correctamente con integrantes y líder",
                        equipoGuardado,
                        HttpStatus.OK
                )
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> update(Long id, Equipo equipoRequest) {
        Optional<Equipo> equipoOpt = equipoRepository.findById(id);

        if (equipoOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Equipo no encontrado", HttpStatus.BAD_REQUEST));
        }

        Equipo equipoExistente = equipoOpt.get();

        if (equipoRequest.getNombreEquipo() == null || equipoRequest.getNombreEquipo().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El nombre del equipo es obligatorio", HttpStatus.BAD_REQUEST));
        }

        String nuevoNombre = equipoRequest.getNombreEquipo().trim();

        Optional<Equipo> equipoMismoNombre = equipoRepository.findAll().stream()
                .filter(e -> e.getNombreEquipo() != null)
                .filter(e -> e.getNombreEquipo().equalsIgnoreCase(nuevoNombre))
                .filter(e -> !Objects.equals(e.getIdEquipo(), id))
                .findFirst();

        if (equipoMismoNombre.isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Ya existe un equipo con ese nombre", HttpStatus.BAD_REQUEST));
        }

        Long nuevoProyectoId = extraerProyectoId(equipoRequest);
        Long nuevoLiderId = extraerLiderId(equipoRequest);
        Set<Long> nuevosMiembrosIds = extraerMiembrosIds(equipoRequest);

        if (nuevoProyectoId == null) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Debes seleccionar un proyecto", HttpStatus.BAD_REQUEST));
        }

        if (nuevoLiderId == null) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Debes seleccionar un líder", HttpStatus.BAD_REQUEST));
        }

        if (nuevosMiembrosIds.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Debes seleccionar al menos un integrante", HttpStatus.BAD_REQUEST));
        }

        if (nuevosMiembrosIds.contains(nuevoLiderId)) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El líder no debe ir también en integrantes", HttpStatus.BAD_REQUEST));
        }

        Optional<Usuario> nuevoLiderOpt = usuarioRepository.findById(nuevoLiderId);
        if (nuevoLiderOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El líder seleccionado no existe", HttpStatus.BAD_REQUEST));
        }

        Optional<Proyecto> nuevoProyectoOpt = proyectoRepository.findById(nuevoProyectoId);
        if (nuevoProyectoOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El proyecto seleccionado no existe", HttpStatus.BAD_REQUEST));
        }

        Usuario nuevoLider = nuevoLiderOpt.get();
        Proyecto nuevoProyecto = nuevoProyectoOpt.get();

        for (Long miembroId : nuevosMiembrosIds) {
            if (usuarioRepository.findById(miembroId).isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new AppiResponse("Uno de los integrantes seleccionados no existe", HttpStatus.BAD_REQUEST));
            }
        }

        List<EquipoUsuario> relacionesActuales = equipoUsuarioRepository.findByEquipoIdEquipo(id);

        Set<Long> usuariosActualesDelEquipo = relacionesActuales.stream()
                .map(rel -> rel.getUsuario().getIdUsuario())
                .collect(Collectors.toSet());

        Set<Long> usuariosNuevosDelEquipo = new HashSet<>(nuevosMiembrosIds);
        usuariosNuevosDelEquipo.add(nuevoLiderId);

        for (Long usuarioId : usuariosNuevosDelEquipo) {
            List<EquipoUsuario> relacionesUsuario = equipoUsuarioRepository.findByUsuarioIdUsuario(usuarioId);

            boolean perteneceAOtroEquipo = relacionesUsuario.stream()
                    .anyMatch(rel -> !Objects.equals(rel.getEquipo().getIdEquipo(), id));

            if (perteneceAOtroEquipo) {
                Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
                String username = usuarioOpt.map(Usuario::getUsername).orElse("usuario");
                return ResponseEntity.badRequest()
                        .body(new AppiResponse(
                                "El usuario " + username + " ya pertenece a otro equipo",
                                HttpStatus.BAD_REQUEST
                        ));
            }
        }

        if (nuevoProyecto.getEquipo() != null && !Objects.equals(nuevoProyecto.getEquipo().getIdEquipo(), id)) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El proyecto ya está asignado a otro equipo", HttpStatus.BAD_REQUEST));
        }

        Optional<Rol> rolLiderOpt = rolRepository.findByNombreIgnoreCase("LIDER");
        Optional<Rol> rolIntegranteOpt = rolRepository.findByNombreIgnoreCase("INTEGRANTE");

        if (rolLiderOpt.isEmpty() || rolIntegranteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AppiResponse(
                            "No se encontraron los roles necesarios en la base de datos",
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }

        List<Proyecto> proyectosConEquipoActual = proyectoRepository.findAll().stream()
                .filter(p -> p.getEquipo() != null)
                .filter(p -> Objects.equals(p.getEquipo().getIdEquipo(), id))
                .toList();

        equipoExistente.setNombreEquipo(nuevoNombre);
        equipoExistente.setDescripcion(equipoRequest.getDescripcion());
        equipoExistente.setLogo(equipoRequest.getLogo());

        if (equipoRequest.getEstatus() != null && !equipoRequest.getEstatus().isBlank()) {
            equipoExistente.setEstatus(equipoRequest.getEstatus());
        } else if (equipoExistente.getEstatus() == null || equipoExistente.getEstatus().isBlank()) {
            equipoExistente.setEstatus("ACTIVO");
        }

        equipoRepository.save(equipoExistente);

        for (Proyecto proyecto : proyectosConEquipoActual) {
            if (!Objects.equals(proyecto.getIdProyecto(), nuevoProyectoId)) {
                proyecto.setEquipo(null);
                if (proyecto.getLider() != null && Objects.equals(proyecto.getLider().getIdUsuario(), nuevoLiderId)) {
                    proyecto.setLider(null);
                }
                proyectoRepository.save(proyecto);
            }
        }

        nuevoProyecto.setEquipo(equipoExistente);
        nuevoProyecto.setLider(nuevoLider);
        proyectoRepository.save(nuevoProyecto);

        equipoUsuarioRepository.deleteAll(relacionesActuales);

        EquipoUsuario relacionLider = new EquipoUsuario();
        relacionLider.setEquipo(equipoExistente);
        relacionLider.setUsuario(nuevoLider);
        relacionLider.setRolEnEquipo("LIDER");
        relacionLider.setFechaUnion(LocalDate.now());
        equipoUsuarioRepository.save(relacionLider);

        nuevoLider.setRol(rolLiderOpt.get());
        usuarioRepository.save(nuevoLider);

        for (Long miembroId : nuevosMiembrosIds) {
            Usuario miembro = usuarioRepository.findById(miembroId).get();

            EquipoUsuario relacion = new EquipoUsuario();
            relacion.setEquipo(equipoExistente);
            relacion.setUsuario(miembro);
            relacion.setRolEnEquipo("INTEGRANTE");
            relacion.setFechaUnion(LocalDate.now());
            equipoUsuarioRepository.save(relacion);

            miembro.setRol(rolIntegranteOpt.get());
            usuarioRepository.save(miembro);
        }

        return ResponseEntity.ok(
                new AppiResponse("Equipo actualizado correctamente", equipoExistente, HttpStatus.OK)
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> delete(Long id) {
        Optional<Equipo> equipoOpt = equipoRepository.findById(id);

        if (equipoOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Equipo no encontrado", HttpStatus.BAD_REQUEST));
        }

        Equipo equipo = equipoOpt.get();
        equipo.setEstatus("INACTIVO");
        equipoRepository.save(equipo);

        return ResponseEntity.ok(
                new AppiResponse("Equipo desactivado correctamente", HttpStatus.OK)
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findDisponiblesParaProyecto() {
        List<Equipo> equiposDisponibles = equipoRepository.findAll()
                .stream()
                .filter(equipo -> "ACTIVO".equalsIgnoreCase(equipo.getEstatus()))
                .filter(equipo ->
                        !proyectoRepository.existsByEquipoIdEquipoAndEstadoNotIgnoreCase(
                                equipo.getIdEquipo(),
                                "CANCELADO"
                        )
                )
                .toList();

        return ResponseEntity.ok(
                new AppiResponse(
                        "Equipos disponibles para proyecto obtenidos correctamente",
                        equiposDisponibles,
                        HttpStatus.OK
                )
        );
    }

    private Long extraerProyectoId(Equipo equipoRequest) {
        try {
            Object proyectoObj = equipoRequest.getClass().getMethod("getProyecto").invoke(equipoRequest);
            if (proyectoObj == null) return null;
            Object idProyecto = proyectoObj.getClass().getMethod("getIdProyecto").invoke(proyectoObj);
            return idProyecto != null ? Long.valueOf(String.valueOf(idProyecto)) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private Long extraerLiderId(Equipo equipoRequest) {
        try {
            Object liderObj = equipoRequest.getClass().getMethod("getLider").invoke(equipoRequest);
            if (liderObj == null) return null;
            Object idUsuario = liderObj.getClass().getMethod("getIdUsuario").invoke(liderObj);
            return idUsuario != null ? Long.valueOf(String.valueOf(idUsuario)) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private Set<Long> extraerMiembrosIds(Equipo equipoRequest) {
        Set<Long> ids = new HashSet<>();

        try {
            Object miembrosObj = equipoRequest.getClass().getMethod("getMiembros").invoke(equipoRequest);
            if (miembrosObj instanceof List<?> lista) {
                for (Object item : lista) {
                    try {
                        Object idUsuario = item.getClass().getMethod("getIdUsuario").invoke(item);
                        if (idUsuario != null) {
                            ids.add(Long.valueOf(String.valueOf(idUsuario)));
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (Exception ignored) {
        }

        return ids;
    }
}