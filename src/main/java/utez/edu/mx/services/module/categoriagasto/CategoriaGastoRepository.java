package utez.edu.mx.services.module.categoriagasto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaGastoRepository extends JpaRepository<CategoriaGasto, Long> {
    boolean existsByNombre(String nombre);
    Optional<CategoriaGasto> findByNombreIgnoreCase(String nombre);
}