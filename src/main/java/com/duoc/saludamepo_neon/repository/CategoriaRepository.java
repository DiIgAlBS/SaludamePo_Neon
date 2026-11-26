package com.duoc.saludamepo_neon.repository;

import com.duoc.saludamepo_neon.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}