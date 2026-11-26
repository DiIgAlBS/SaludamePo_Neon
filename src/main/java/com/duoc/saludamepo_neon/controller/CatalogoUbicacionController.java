package com.duoc.saludamepo_neon.controller;

import com.duoc.saludamepo_neon.model.Categoria;
import com.duoc.saludamepo_neon.model.Comuna;
import com.duoc.saludamepo_neon.model.Region;
import com.duoc.saludamepo_neon.repository.CategoriaRepository;
import com.duoc.saludamepo_neon.repository.ComunaRepository;
import com.duoc.saludamepo_neon.repository.RegionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "6. Categorías y Ubicación", description = "Rubros de famosos, regiones y comunas de Chile")
public class CatalogoUbicacionController {

    private final CategoriaRepository categoriaRepository;
    private final RegionRepository regionRepository;
    private final ComunaRepository comunaRepository;

    @GetMapping("/categorias")
    @Operation(summary = "Listar categorías de famosos (Público)")
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    @PostMapping("/categorias")
    @Operation(summary = "Crear nueva categoría (Solo ADMIN)")
    public ResponseEntity<?> crearCategoria(@Valid @RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaRepository.save(categoria));
    }

    @GetMapping("/regiones")
    @Operation(summary = "Listar regiones de Chile (Público)")
    public List<Region> listarRegiones() {
        return regionRepository.findAll();
    }

    @GetMapping("/comunas")
    @Operation(summary = "Listar todas las comunas (Público)")
    public List<Comuna> listarComunas() {
        return comunaRepository.findAll();
    }

    @GetMapping("/comunas/region/{regionId}")
    @Operation(summary = "Listar comunas filtradas por región (Público)")
    public List<Comuna> listarComunasPorRegion(@PathVariable Long regionId) {
        return comunaRepository.findByRegionId(regionId);
    }
}