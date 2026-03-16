package utez.edu.mx.services.module.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;
import utez.edu.mx.services.module.rol.Rol;
import utez.edu.mx.services.module.rol.RolRepository;
import utez.edu.mx.services.module.usuario.dto.UsuarioDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            RolRepository rolRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
    }

    private boolean passwordSegura(String password) {
        if (password == null || password.length() < 8) return false;
        if (password.contains(" ")) return false;
        if (!password.matches(".*[A-Z].*")) return false;
        if (!password.matches(".*[a-z].*")) return false;
        if (!password.matches(".*[0-9].*")) return false;
        if (!password.matches(".*[@#$%&*!].*")) return false;
        return true;
    }
    public boolean passwordSeguraPublic(String password) {
        return passwordSegura(password);
    }


    private Optional<Rol> obtenerRolIntegrante() {
        return rolRepository.findByNombreIgnoreCase("INTEGRANTE");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        List<UsuarioDTO> usuarios = usuarioRepository.findAll()
                .stream()
                .map(UsuarioDTO::new)
                .toList();

        return ResponseEntity.ok(
                new AppiResponse("Operación exitosa", usuarios, HttpStatus.OK)
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if (usuario.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Usuario no encontrado", HttpStatus.BAD_REQUEST));
        }

        return ResponseEntity.ok(
                new AppiResponse("Operación exitosa", new UsuarioDTO(usuario.get()), HttpStatus.OK)
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> miPerfil(String username) {
        Optional<Usuario> usuario = usuarioRepository.findByUsername(username);

        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppiResponse("Usuario no encontrado", HttpStatus.NOT_FOUND));
        }

        return ResponseEntity.ok(
                new AppiResponse("Perfil obtenido correctamente", new UsuarioDTO(usuario.get()), HttpStatus.OK)
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> save(Usuario usuario) {
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El username ya está en uso", HttpStatus.BAD_REQUEST));
        }

        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El correo ya está en uso", HttpStatus.BAD_REQUEST));
        }

        if (!passwordSegura(usuario.getPassword())) {
            return ResponseEntity.badRequest().body(new AppiResponse(
                    "La contraseña no cumple con los requisitos: mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial (@#$%&*!)",
                    HttpStatus.BAD_REQUEST
            ));
        }

        Optional<Rol> rolIntegrante = obtenerRolIntegrante();
        if (rolIntegrante.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AppiResponse(
                            "No existe el rol INTEGRANTE en la base de datos",
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setEstatus("ACTIVO");
        usuario.setIntentosFallidos(0);
        usuario.setFechaBloqueo(null);
        usuario.setRol(rolIntegrante.get());

        if (usuario.getSalario() == null) {
            usuario.setSalario(BigDecimal.ZERO);
        }

        Usuario saved = usuarioRepository.save(usuario);

        return ResponseEntity.ok(
                new AppiResponse(
                        "Usuario registrado exitosamente",
                        new UsuarioDTO(saved),
                        HttpStatus.OK
                )
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> update(Long id, Usuario usuario) {
        Optional<Usuario> existing = usuarioRepository.findById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Usuario no encontrado", HttpStatus.BAD_REQUEST));
        }

        Usuario u = existing.get();
        u.setNombre(usuario.getNombre());
        u.setApellidoPaterno(usuario.getApellidoPaterno());
        u.setApellidoMaterno(usuario.getApellidoMaterno());
        u.setCorreo(usuario.getCorreo());
        u.setSalario(usuario.getSalario());

        if (usuario.getRol() != null) {
            u.setRol(usuario.getRol());
        }

        Usuario updated = usuarioRepository.save(u);

        return ResponseEntity.ok(
                new AppiResponse(
                        "Usuario actualizado exitosamente",
                        new UsuarioDTO(updated),
                        HttpStatus.OK
                )
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> cambiarPassword(Long id, String newPassword) {
        Optional<Usuario> existing = usuarioRepository.findById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Usuario no encontrado", HttpStatus.BAD_REQUEST));
        }

        if (!passwordSegura(newPassword)) {
            return ResponseEntity.badRequest().body(new AppiResponse(
                    "La contraseña no cumple con los requisitos: mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial (@#$%&*!)",
                    HttpStatus.BAD_REQUEST
            ));
        }

        Usuario u = existing.get();
        u.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(u);

        return ResponseEntity.ok(
                new AppiResponse("Contraseña actualizada exitosamente", HttpStatus.OK)
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> delete(Long id) {
        Optional<Usuario> existing = usuarioRepository.findById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Usuario no encontrado", HttpStatus.BAD_REQUEST));
        }

        Usuario u = existing.get();
        u.setEstatus("INACTIVO");
        usuarioRepository.save(u);

        return ResponseEntity.ok(
                new AppiResponse("Usuario desactivado exitosamente", HttpStatus.OK)
        );
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findEntityByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Transactional
    public void registrarIntentoFallido(String username) {
        usuarioRepository.findByUsername(username).ifPresent(u -> {
            u.setIntentosFallidos(u.getIntentosFallidos() + 1);

            if (u.getIntentosFallidos() >= 3) {
                u.setFechaBloqueo(LocalDateTime.now().plusMinutes(30));
                u.setEstatus("BLOQUEADO");
            }

            usuarioRepository.save(u);
        });
    }

    @Transactional
    public void resetearIntentos(String username) {
        usuarioRepository.findByUsername(username).ifPresent(u -> {
            u.setIntentosFallidos(0);
            u.setFechaBloqueo(null);

            if ("BLOQUEADO".equals(u.getEstatus())) {
                u.setEstatus("ACTIVO");
            }

            usuarioRepository.save(u);
        });
    }

    @Transactional(readOnly = true)
    public boolean estaBloqueado(String username) {
        return usuarioRepository.findByUsername(username).map(u -> {
            if (!"BLOQUEADO".equals(u.getEstatus())) {
                return false;
            }

            if (u.getFechaBloqueo() != null && LocalDateTime.now().isAfter(u.getFechaBloqueo())) {
                resetearIntentos(username);
                return false;
            }

            return true;
        }).orElse(false);
    }
}