package utez.edu.mx.services.module.material;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;
import utez.edu.mx.services.module.presupuesto.Presupuesto;
import utez.edu.mx.services.module.presupuesto.PresupuestoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final PresupuestoRepository presupuestoRepository;

    public MaterialService(MaterialRepository materialRepository, PresupuestoRepository presupuestoRepository) {
        this.materialRepository = materialRepository;
        this.presupuestoRepository = presupuestoRepository;
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
        // Validar cantidad no negativa
        if (material.getCantidad() < 0)
            return ResponseEntity.badRequest().body(new AppiResponse(
                    "La cantidad no puede ser negativa", HttpStatus.BAD_REQUEST));

        // Validar precio mayor a 0
        if (material.getPrecio().compareTo(BigDecimal.ZERO) <= 0)
            return ResponseEntity.badRequest().body(new AppiResponse(
                    "El costo unitario debe ser mayor a cero", HttpStatus.BAD_REQUEST));

        // Calcular total
        BigDecimal total = material.getPrecio().multiply(new BigDecimal(material.getCantidad()));
        material.setTotal(total);

        // Verificar presupuesto del proyecto
        Long idProyecto = material.getProyecto().getIdProyecto();
        Optional<Presupuesto> presupuestoOpt = presupuestoRepository.findByProyectoIdProyecto(idProyecto);
        if (presupuestoOpt.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse(
                    "El proyecto no tiene presupuesto asignado", HttpStatus.BAD_REQUEST));

        Presupuesto presupuesto = presupuestoOpt.get();
        if (total.compareTo(presupuesto.getMontoDisponible()) > 0)
            return ResponseEntity.badRequest().body(new AppiResponse(
                    "Presupuesto insuficiente para asignar el material. Disponible: $" + presupuesto.getMontoDisponible(),
                    HttpStatus.BAD_REQUEST));

        // Descontar del presupuesto
        presupuesto.setMontoUtilizado(presupuesto.getMontoUtilizado().add(total));
        presupuesto.setMontoDisponible(presupuesto.getMontoDisponible().subtract(total));
        presupuestoRepository.save(presupuesto);

        Material saved = materialRepository.save(material);
        return ResponseEntity.ok(new AppiResponse("Material registrado exitosamente", saved, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> update(Long id, Material material) {
        Optional<Material> existing = materialRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Material no encontrado", HttpStatus.BAD_REQUEST));

        // Validar cantidad no negativa
        if (material.getCantidad() < 0)
            return ResponseEntity.badRequest().body(new AppiResponse(
                    "La cantidad no puede ser negativa", HttpStatus.BAD_REQUEST));

        // Validar precio mayor a 0
        if (material.getPrecio().compareTo(BigDecimal.ZERO) <= 0)
            return ResponseEntity.badRequest().body(new AppiResponse(
                    "El costo unitario debe ser mayor a cero", HttpStatus.BAD_REQUEST));

        Material m = existing.get();
        BigDecimal totalAnterior = m.getTotal();
        BigDecimal nuevoTotal = material.getPrecio().multiply(new BigDecimal(material.getCantidad()));

        // Ajustar presupuesto si cambió el total
        if (nuevoTotal.compareTo(totalAnterior) != 0) {
            Long idProyecto = m.getProyecto().getIdProyecto();
            Optional<Presupuesto> presupuestoOpt = presupuestoRepository.findByProyectoIdProyecto(idProyecto);
            if (presupuestoOpt.isPresent()) {
                Presupuesto presupuesto = presupuestoOpt.get();
                // Revertir el total anterior y aplicar el nuevo
                BigDecimal diferencia = nuevoTotal.subtract(totalAnterior);
                if (diferencia.compareTo(BigDecimal.ZERO) > 0 &&
                        diferencia.compareTo(presupuesto.getMontoDisponible()) > 0)
                    return ResponseEntity.badRequest().body(new AppiResponse(
                            "Presupuesto insuficiente para actualizar el material. Disponible: $" + presupuesto.getMontoDisponible(),
                            HttpStatus.BAD_REQUEST));

                presupuesto.setMontoUtilizado(presupuesto.getMontoUtilizado().add(diferencia));
                presupuesto.setMontoDisponible(presupuesto.getMontoDisponible().subtract(diferencia));
                presupuestoRepository.save(presupuesto);
            }
        }

        m.setNombre(material.getNombre());
        m.setCantidad(material.getCantidad());
        m.setPrecio(material.getPrecio());
        m.setTotal(nuevoTotal);
        m.setCategoria(material.getCategoria());
        return ResponseEntity.ok(new AppiResponse("Material actualizado exitosamente", materialRepository.save(m), HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> delete(Long id) {
        Optional<Material> existing = materialRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Material no encontrado", HttpStatus.BAD_REQUEST));

        // Revertir el costo al presupuesto al eliminar
        Material m = existing.get();
        Long idProyecto = m.getProyecto().getIdProyecto();
        presupuestoRepository.findByProyectoIdProyecto(idProyecto).ifPresent(presupuesto -> {
            presupuesto.setMontoUtilizado(presupuesto.getMontoUtilizado().subtract(m.getTotal()));
            presupuesto.setMontoDisponible(presupuesto.getMontoDisponible().add(m.getTotal()));
            presupuestoRepository.save(presupuesto);
        });

        materialRepository.deleteById(id);
        return ResponseEntity.ok(new AppiResponse("Material eliminado exitosamente", HttpStatus.OK));
    }
}