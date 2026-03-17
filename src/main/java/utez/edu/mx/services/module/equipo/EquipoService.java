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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", equipoRepository.findAll(), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<Equipo> equipo = equipoRepository.findById(id);

        if (equipo.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Equipo no encontrado", HttpStatus.BAD_REQUEST));
        }

        return ResponseEntity.ok(new AppiResponse("Operación exitosa", equipo.get(), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findMiEquipo(String username) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppiResponse("Usuario no encontrado", HttpStatus.NOT_FOUND));
        }

        List<EquipoUsuario> relaciones = equipoUsuarioRepository.findByUsuarioIdUsuario(usuarioOpt.get().getIdUsuario());
        List<Equipo> equipos = relaciones.stream().map(EquipoUsuario::getEquipo).toList();

        return ResponseEntity.ok(new AppiResponse("Operación exitosa", equipos, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findIntegrantesByEquipo(Long idEquipo) {
        List<Usuario> integrantes = equipoUsuarioRepository.findByEquipoIdEquipo(idEquipo)
                .stream()
                .map(EquipoUsuario::getUsuario)
                .toList();

        return ResponseEntity.ok(new AppiResponse("Operación exitosa", integrantes, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findMisIntegrantes(String username) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppiResponse("Usuario no encontrado", HttpStatus.NOT_FOUND));
        }

        List<EquipoUsuario> misRelaciones = equipoUsuarioRepository.findByUsuarioIdUsuario(usuarioOpt.get().getIdUsuario());

        if (misRelaciones.isEmpty()) {
            return ResponseEntity.ok(new AppiResponse("Operación exitosa", List.of(), HttpStatus.OK));
        }

        Long idEquipo = misRelaciones.get(0).getEquipo().getIdEquipo();

        List<Usuario> integrantes = equipoUsuarioRepository.findByEquipoIdEquipo(idEquipo)
                .stream()
                .map(EquipoUsuario::getUsuario)
                .toList();

        return ResponseEntity.ok(new AppiResponse("Operación exitosa", integrantes, HttpStatus.OK));
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

        return ResponseEntity.ok(new AppiResponse("Equipo registrado correctamente", saved, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> crearEquipoConMiembros(CreateTeamRequestDTO dto) {
        if (equipoRepository.existsByNombreEquipo(dto.getNombreEquipo())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Ya existe un equipo con ese nombre", HttpStatus.BAD_REQUEST));
        }

        if (dto.getIntegrantesIds() == null || dto.getIntegrantesIds().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Debes seleccionar al menos un integrante", HttpStatus.BAD_REQUEST));
        }

        Set<Long> idsUnicos = new HashSet<>(dto.getIntegrantesIds());
        if (idsUnicos.size() != dto.getIntegrantesIds().size()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("No puedes repetir integrantes", HttpStatus.BAD_REQUEST));
        }

        Optional<Rol> rolLiderOpt = rolRepository.findByNombreIgnoreCase("LIDER");
        Optional<Rol> rolIntegranteOpt = rolRepository.findByNombreIgnoreCase("INTEGRANTE");

        if (rolLiderOpt.isEmpty() || rolIntegranteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AppiResponse("No se encontraron los roles necesarios en la base de datos", HttpStatus.INTERNAL_SERVER_ERROR));
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

        if (equipoUsuarioRepository.existsByUsuarioIdUsuario(lider.getIdUsuario())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse(
                            "El líder " + lider.getUsername() + " ya pertenece a un equipo",
                            HttpStatus.BAD_REQUEST
                    ));
        }

        Equipo equipo = new Equipo();
        equipo.setNombreEquipo(dto.getNombreEquipo());
        equipo.setLogo(dto.getLogo());
        equipo.setFechaCreacion(LocalDate.now());
        equipo.setEstatus("ACTIVO");

        Equipo equipoGuardado = equipoRepository.save(equipo);

        // Relacionar el proyecto con el equipo guardado
        Proyecto proyecto = proyectoOpt.get();
        proyecto.setEquipo(equipoGuardado);
        proyectoRepository.save(proyecto);

        // Guardar líder en relación equipo-usuario
        EquipoUsuario relacionLider = new EquipoUsuario();
        relacionLider.setEquipo(equipoGuardado);
        relacionLider.setUsuario(lider);
        equipoUsuarioRepository.save(relacionLider);

        lider.setRol(rolLiderOpt.get());
        usuarioRepository.save(lider);

        // Guardar integrantes
        for (Long idUsuario : dto.getIntegrantesIds()) {
            if (idUsuario.equals(dto.getIdLider())) {
                continue;
            }

            Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new AppiResponse("Uno de los usuarios seleccionados no existe", HttpStatus.BAD_REQUEST));
            }

            Usuario usuario = usuarioOpt.get();

            if (equipoUsuarioRepository.existsByUsuarioIdUsuario(usuario.getIdUsuario())) {
                return ResponseEntity.badRequest()
                        .body(new AppiResponse(
                                "El usuario " + usuario.getUsername() + " ya pertenece a un equipo",
                                HttpStatus.BAD_REQUEST
                        ));
            }

            EquipoUsuario relacion = new EquipoUsuario();
            relacion.setEquipo(equipoGuardado);
            relacion.setUsuario(usuario);
            equipoUsuarioRepository.save(relacion);

            usuario.setRol(rolIntegranteOpt.get());
            usuarioRepository.save(usuario);
        }

        return ResponseEntity.ok(
                new AppiResponse("Equipo creado correctamente con integrantes y líder", equipoGuardado, HttpStatus.OK)
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> update(Long id, Equipo equipo) {
        Optional<Equipo> equipoOpt = equipoRepository.findById(id);

        if (equipoOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Equipo no encontrado", HttpStatus.BAD_REQUEST));
        }

        Equipo existente = equipoOpt.get();
        existente.setNombreEquipo(equipo.getNombreEquipo());
        existente.setDescripcion(equipo.getDescripcion());
        existente.setLogo(equipo.getLogo());
        existente.setEstatus(equipo.getEstatus());

        Equipo updated = equipoRepository.save(existente);

        return ResponseEntity.ok(new AppiResponse("Equipo actualizado correctamente", updated, HttpStatus.OK));
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

        return ResponseEntity.ok(new AppiResponse("Equipo desactivado correctamente", HttpStatus.OK));
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
                new AppiResponse("Equipos disponibles para proyecto obtenidos correctamente", equiposDisponibles, HttpStatus.OK)
        );
    }
}