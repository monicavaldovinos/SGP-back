package utez.edu.mx.services.module.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;

import java.time.LocalDate;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // GET ALL
    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        AppiResponse response = new AppiResponse(
                "Operación exitosa",
                usuarioRepository.findAll(),
                HttpStatus.OK
        );
        return new ResponseEntity<>(response, response.getStatus());
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Usuario found = usuarioRepository.findById(id).orElse(null);
        AppiResponse response;
        if (found != null) {
            response = new AppiResponse("Operación exitosa", found, HttpStatus.OK);
        } else {
            response = new AppiResponse("Usuario no encontrado", true, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, response.getStatus());
    }

    // POST
    @Transactional
    public ResponseEntity<AppiResponse> save(Usuario usuario) {
        // Verificar correo duplicado
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            AppiResponse response = new AppiResponse("El correo ya está registrado", true, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, response.getStatus());
        }
        // Verificar username duplicado
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            AppiResponse response = new AppiResponse("El username ya está en uso", true, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, response.getStatus());
        }
        // Asignar fecha de registro automáticamente
        usuario.setFechaRegistro(LocalDate.now());
        // Asignar estatus activo por defecto
        usuario.setEstatus("ACTIVO");

        AppiResponse response = new AppiResponse(
                "Usuario registrado correctamente",
                usuarioRepository.save(usuario),
                HttpStatus.CREATED
        );
        return new ResponseEntity<>(response, response.getStatus());
    }

    // PUT
    @Transactional
    public ResponseEntity<AppiResponse> update(Usuario usuario) {
        if (!usuarioRepository.existsById(usuario.getIdUsuario())) {
            AppiResponse response = new AppiResponse("Usuario no encontrado", true, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, response.getStatus());
        }
        AppiResponse response = new AppiResponse(
                "Usuario actualizado correctamente",
                usuarioRepository.save(usuario),
                HttpStatus.OK
        );
        return new ResponseEntity<>(response, response.getStatus());
    }

    // DELETE (cambio de estatus, no borrado físico)
    @Transactional
    public ResponseEntity<AppiResponse> delete(Long id) {
        Usuario found = usuarioRepository.findById(id).orElse(null);
        if (found == null) {
            AppiResponse response = new AppiResponse("Usuario no encontrado", true, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, response.getStatus());
        }
        // Borrado lógico: cambiar estatus
        found.setEstatus("INACTIVO");
        usuarioRepository.save(found);
        AppiResponse response = new AppiResponse("Usuario desactivado correctamente", HttpStatus.OK);
        return new ResponseEntity<>(response, response.getStatus());
    }
}