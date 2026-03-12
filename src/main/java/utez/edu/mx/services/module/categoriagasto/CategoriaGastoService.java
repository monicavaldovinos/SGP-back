package utez.edu.mx.services.module.categoriagasto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;

import java.util.Optional;

@Service
public class CategoriaGastoService {

    private final CategoriaGastoRepository categoriaGastoRepository;

    public CategoriaGastoService(CategoriaGastoRepository categoriaGastoRepository) {
        this.categoriaGastoRepository = categoriaGastoRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", categoriaGastoRepository.findAll(), HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<CategoriaGasto> c = categoriaGastoRepository.findById(id);
        if (c.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Categoría no encontrada", HttpStatus.BAD_REQUEST));
        return ResponseEntity.ok(new AppiResponse("Operación exitosa", c.get(), HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> save(CategoriaGasto categoria) {
        if (categoriaGastoRepository.existsByNombre(categoria.getNombre()))
            return ResponseEntity.badRequest().body(new AppiResponse("Ya existe una categoría con ese nombre", HttpStatus.BAD_REQUEST));
        CategoriaGasto saved = categoriaGastoRepository.save(categoria);
        return ResponseEntity.ok(new AppiResponse("Categoría registrada exitosamente", saved, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> update(Long id, CategoriaGasto categoria) {
        Optional<CategoriaGasto> existing = categoriaGastoRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Categoría no encontrada", HttpStatus.BAD_REQUEST));
        CategoriaGasto c = existing.get();
        c.setNombre(categoria.getNombre());
        c.setDescripcion(categoria.getDescripcion());
        return ResponseEntity.ok(new AppiResponse("Categoría actualizada exitosamente", categoriaGastoRepository.save(c), HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> delete(Long id) {
        Optional<CategoriaGasto> existing = categoriaGastoRepository.findById(id);
        if (existing.isEmpty())
            return ResponseEntity.badRequest().body(new AppiResponse("Categoría no encontrada", HttpStatus.BAD_REQUEST));
        categoriaGastoRepository.deleteById(id);
        return ResponseEntity.ok(new AppiResponse("Categoría eliminada exitosamente", HttpStatus.OK));
    }
}