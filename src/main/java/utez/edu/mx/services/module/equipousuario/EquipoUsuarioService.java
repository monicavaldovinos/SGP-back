package utez.edu.mx.services.module.equipousuario;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;
import utez.edu.mx.services.module.equipo.Equipo;
import utez.edu.mx.services.module.equipo.EquipoRepository;
import utez.edu.mx.services.module.equipousuario.dto.CambiarLiderRequestDTO;
import utez.edu.mx.services.module.rol.Rol;
import utez.edu.mx.services.module.rol.RolRepository;
import utez.edu.mx.services.module.usuario.Usuario;
import utez.edu.mx.services.module.usuario.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EquipoUsuarioService {

    private final EquipoUsuarioRepository equipoUsuarioRepository;
    private final EquipoRepository equipoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public EquipoUsuarioService(
            EquipoUsuarioRepository equipoUsuarioRepository,
            EquipoRepository equipoRepository,
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository
    ) {
        this.equipoUsuarioRepository = equipoUsuarioRepository;
        this.equipoRepository = equipoRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Transactional
    public ResponseEntity<AppiResponse> asignarUsuarioAEquipo(Long idEquipo, Long idUsuario) {
        Optional<Equipo> equipoOpt = equipoRepository.findById(idEquipo);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (equipoOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Equipo no encontrado", HttpStatus.BAD_REQUEST));
        }

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Usuario no encontrado", HttpStatus.BAD_REQUEST));
        }

        if (equipoUsuarioRepository.existsByUsuarioIdUsuario(idUsuario)) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El usuario ya pertenece a un equipo", HttpStatus.BAD_REQUEST));
        }

        EquipoUsuario relacion = new EquipoUsuario();
        relacion.setEquipo(equipoOpt.get());
        relacion.setUsuario(usuarioOpt.get());

        equipoUsuarioRepository.save(relacion);

        return ResponseEntity.ok(
                new AppiResponse("Usuario asignado al equipo correctamente", HttpStatus.OK)
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> cambiarLider(CambiarLiderRequestDTO dto) {
        Optional<Rol> rolLiderOpt = rolRepository.findByNombreIgnoreCase("LIDER");
        Optional<Rol> rolIntegranteOpt = rolRepository.findByNombreIgnoreCase("INTEGRANTE");

        if (rolLiderOpt.isEmpty() || rolIntegranteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AppiResponse("No se encontraron los roles necesarios", HttpStatus.INTERNAL_SERVER_ERROR));
        }

        Optional<EquipoUsuario> liderActualRel = equipoUsuarioRepository
                .findByEquipoIdEquipoAndUsuarioIdUsuario(dto.getIdEquipo(), dto.getIdLiderActual());

        Optional<EquipoUsuario> nuevoLiderRel = equipoUsuarioRepository
                .findByEquipoIdEquipoAndUsuarioIdUsuario(dto.getIdEquipo(), dto.getIdNuevoLider());

        if (liderActualRel.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El líder actual no pertenece a ese equipo", HttpStatus.BAD_REQUEST));
        }

        if (nuevoLiderRel.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El nuevo líder no pertenece a ese equipo", HttpStatus.BAD_REQUEST));
        }

        Usuario liderActual = liderActualRel.get().getUsuario();
        Usuario nuevoLider = nuevoLiderRel.get().getUsuario();

        if (!"LIDER".equalsIgnoreCase(liderActual.getRol().getNombre())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El usuario actual no tiene rol de líder", HttpStatus.BAD_REQUEST));
        }

        liderActual.setRol(rolIntegranteOpt.get());
        nuevoLider.setRol(rolLiderOpt.get());

        usuarioRepository.save(liderActual);
        usuarioRepository.save(nuevoLider);

        return ResponseEntity.ok(
                new AppiResponse("Liderazgo transferido correctamente", HttpStatus.OK)
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> quitarUsuarioDeEquipo(Long idEquipo, Long idUsuario) {
        Optional<EquipoUsuario> relacionOpt =
                equipoUsuarioRepository.findByEquipoIdEquipoAndUsuarioIdUsuario(idEquipo, idUsuario);

        if (relacionOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("La relación equipo-usuario no existe", HttpStatus.BAD_REQUEST));
        }

        Usuario usuario = relacionOpt.get().getUsuario();

        if ("LIDER".equalsIgnoreCase(usuario.getRol().getNombre())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse(
                            "No puedes eliminar al líder del equipo sin antes asignar otro líder",
                            HttpStatus.BAD_REQUEST
                    ));
        }

        equipoUsuarioRepository.delete(relacionOpt.get());

        Optional<Rol> rolIntegranteOpt = rolRepository.findByNombreIgnoreCase("INTEGRANTE");
        rolIntegranteOpt.ifPresent(usuario::setRol);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(
                new AppiResponse("Usuario removido del equipo correctamente", HttpStatus.OK)
        );
    }
}