package com.duoc.saludamepo_neon.repository;

import com.duoc.saludamepo_neon.model.Famoso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FamosoRepository extends JpaRepository<Famoso, Long> {
    List<Famoso> findByActivoTrue();
    List<Famoso> findByCategoriaIdAndActivoTrue(Long categoriaId);
    List<Famoso> findByNombreContainingIgnoreCase(String nombre);
}