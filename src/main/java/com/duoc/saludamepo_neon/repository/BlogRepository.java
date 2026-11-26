package com.duoc.saludamepo_neon.repository;

import com.duoc.saludamepo_neon.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findAllByOrderByFechaPublicacionDesc();
}