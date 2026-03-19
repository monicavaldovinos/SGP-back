package utez.edu.mx.services.module.rol;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // GET ALL
    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        AppiResponse response = new AppiResponse(
                "Operación exitosa",
                rolRepository.findAll(),
                HttpStatus.OK
        );
        return new ResponseEntity<>(response, response.getStatus());
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Rol found = rolRepository.findById(id).orElse(null);
        AppiResponse response;
        if (found != null) {
            response = new AppiResponse("Operación exitosa", found, HttpStatus.OK);
        } else {
            response = new AppiResponse("Rol no encontrado", true, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, response.getStatus());
    }

    // POST
    @Transactional
    public ResponseEntity<AppiResponse> save(Rol rol) {
        AppiResponse response = new AppiResponse(
                "Rol registrado correctamente",
                rolRepository.save(rol),
                HttpStatus.CREATED
        );
        return new ResponseEntity<>(response, response.getStatus());
    }

    // PUT
    @Transactional
    public ResponseEntity<AppiResponse> update(Rol rol) {
        if (!rolRepository.existsById(rol.getIdRol())) {
            AppiResponse response = new AppiResponse("Rol no encontrado", true, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, response.getStatus());
        }
        AppiResponse response = new AppiResponse(
                "Rol actualizado correctamente",
                rolRepository.save(rol),
                HttpStatus.OK
        );
        return new ResponseEntity<>(response, response.getStatus());
    }

    // DELETE
    @Transactional
    public ResponseEntity<AppiResponse> delete(Long id) {
        if (!rolRepository.existsById(id)) {
            AppiResponse response = new AppiResponse("Rol no encontrado", true, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, response.getStatus());
        }
        rolRepository.deleteById(id);
        AppiResponse response = new AppiResponse("Rol eliminado correctamente", HttpStatus.OK);
        return new ResponseEntity<>(response, response.getStatus());
    }
}