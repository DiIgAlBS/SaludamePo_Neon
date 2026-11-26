package com.duoc.saludamepo_neon.repository;

import com.duoc.saludamepo_neon.model.VideoFamoso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VideoFamosoRepository extends JpaRepository<VideoFamoso, Long> {
    List<VideoFamoso> findByFamosoId(Long famosoId);
}