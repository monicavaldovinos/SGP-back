package utez.edu.mx.services.module.material;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", materialRepository.findAll(), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<Material> m = materialRepository.findById(id);
        if (m.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Material no encontrado", HttpStatus.BAD_REQUEST));
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", m.get(), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByProyecto(Long idProyecto) {
        List<Material> materiales = materialRepository.findByProyectoIdProyecto(idProyecto);
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", materiales, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> save(Material material) {
        // Calcular total automáticamente
        BigDecimal total = material.getPrecio().multiply(new BigDecimal(material.getCantidad()));
        material.setTotal(total);
        Material saved = materialRepository.save(material);
        return ResponseEntity.ok(new AppiResponse("Material registrado exitosamente", saved, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> update(Long id, Material material) {
        Optional<Material> existing = materialRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Material no encontrado", HttpStatus.BAD_REQUEST));

        Material m = existing.get();
        m.setNombre(material.getNombre());
        m.setCantidad(material.getCantidad());
        m.setPrecio(material.getPrecio());
        m.setTotal(material.getPrecio().multiply(new BigDecimal(material.getCantidad())));
        m.setCategoria(material.getCategoria());
        return ResponseEntity.ok(new AppiResponse("Material actualizado exitosamente", materialRepository.save(m), HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> delete(Long id) {
        Optional<Material> existing = materialRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Material no encontrado", HttpStatus.BAD_REQUEST));
        materialRepository.deleteById(id);
        return ResponseEntity.ok(new AppiResponse("Material eliminado exitosamente", HttpStatus.OK));
    }
}