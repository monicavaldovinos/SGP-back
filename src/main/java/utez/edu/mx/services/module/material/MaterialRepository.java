package utez.edu.mx.services.module.material;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByProyectoIdProyecto(Long idProyecto);
    List<Material> findByCategoriaIdCategoria(Long idCategoria);
}