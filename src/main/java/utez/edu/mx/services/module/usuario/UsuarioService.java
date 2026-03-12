package utez.edu.mx.services.module.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", usuarioRepository.findAll(), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<Usuario> u = usuarioRepository.findById(id);
        if (u.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Usuario no encontrado", HttpStatus.BAD_REQUEST));
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", u.get(), HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> save(Usuario usuario) {
        if (usuarioRepository.existsByUsername(usuario.getUsername()))
            return ResponseEntity.badRequest().body(new AppiResponse("El username ya está en uso", HttpStatus.BAD_REQUEST));
        if (usuarioRepository.existsByCorreo(usuario.getCorreo()))
            return ResponseEntity.badRequest().body(new AppiResponse("El correo ya está en uso", HttpStatus.BAD_REQUEST));

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setEstatus("ACTIVO");
        Usuario saved = usuarioRepository.save(usuario);
        return ResponseEntity.ok(new AppiResponse("Usuario registrado exitosamente", saved, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> update(Long id, Usuario usuario) {
        Optional<Usuario> existing = usuarioRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Usuario no encontrado", HttpStatus.BAD_REQUEST));

        Usuario u = existing.get();
        u.setNombre(usuario.getNombre());
        u.setApellidoPaterno(usuario.getApellidoPaterno());
        u.setApellidoMaterno(usuario.getApellidoMaterno());
        u.setCorreo(usuario.getCorreo());
        u.setSalario(usuario.getSalario());
        u.setRol(usuario.getRol());
        return ResponseEntity.ok(new AppiResponse("Usuario actualizado exitosamente", usuarioRepository.save(u), HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> cambiarPassword(Long id, String newPassword) {
        Optional<Usuario> existing = usuarioRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Usuario no encontrado", HttpStatus.BAD_REQUEST));

        Usuario u = existing.get();
        u.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(u);
        return ResponseEntity.ok(new AppiResponse("Contraseña actualizada exitosamente", HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> delete(Long id) {
        Optional<Usuario> existing = usuarioRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Usuario no encontrado", HttpStatus.BAD_REQUEST));

        Usuario u = existing.get();
        u.setEstatus("INACTIVO");
        usuarioRepository.save(u);
        return ResponseEntity.ok(new AppiResponse("Usuario desactivado exitosamente", HttpStatus.OK));
    }
}