package com.duoc.saludamepo_neon.repository;

import com.duoc.saludamepo_neon.model.AudioFamoso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AudioFamosoRepository extends JpaRepository<AudioFamoso, Long> {
    List<AudioFamoso> findByFamosoId(Long famosoId);
}