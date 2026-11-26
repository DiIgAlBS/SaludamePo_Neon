package com.duoc.saludamepo_neon.repository;

import com.duoc.saludamepo_neon.model.FotoFamoso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FotoFamosoRepository extends JpaRepository<FotoFamoso, Long> {
    List<FotoFamoso> findByFamosoId(Long famosoId);
}