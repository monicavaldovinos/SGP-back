package utez.edu.mx.services.module.categoriagasto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaGastoRepository extends JpaRepository<CategoriaGasto, Long> {
    boolean existsByNombre(String nombre);
}