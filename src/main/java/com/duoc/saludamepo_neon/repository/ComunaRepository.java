package com.duoc.saludamepo_neon.repository;

import com.duoc.saludamepo_neon.model.Comuna;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComunaRepository extends JpaRepository<Comuna, Long> {
    List<Comuna> findByRegionId(Long regionId);
}