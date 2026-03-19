package utez.edu.mx.services.module.equipo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    boolean existsByNombreEquipo(String nombreEquipo);
    List<Equipo> findByEstatusIgnoreCase(String estatus);
}