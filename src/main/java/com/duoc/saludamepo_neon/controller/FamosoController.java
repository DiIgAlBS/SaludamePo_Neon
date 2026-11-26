package com.duoc.saludamepo_neon.controller;

import com.duoc.saludamepo_neon.model.*;
import com.duoc.saludamepo_neon.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/famosos")
@RequiredArgsConstructor
@Tag(name = "3. Famosos y Multimedia", description = "Gestión del catálogo de artistas, fotos, videos y audios")
public class FamosoController {

    private final FamosoRepository famosoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FotoFamosoRepository fotoRepository;
    private final VideoFamosoRepository videoRepository;
    private final AudioFamosoRepository audioRepository;

    @GetMapping
    @Operation(summary = "Listar todos los famosos activos (Público)")
    public List<Famoso> listarActivos() {
        return famosoRepository.findByActivoTrue();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de un famoso con sus fotos, videos y audios (Público)")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return famosoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Filtrar famosos por categoría (Público)")
    public List<Famoso> listarPorCategoria(@PathVariable Long categoriaId) {
        return famosoRepository.findByCategoriaIdAndActivoTrue(categoriaId);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo famoso (Solo ADMIN)")
    public ResponseEntity<?> crear(@Valid @RequestBody Famoso famoso) {
        if (famoso.getCategoria() != null && famoso.getCategoria().getId() != null) {
            Categoria cat = categoriaRepository.findById(famoso.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            famoso.setCategoria(cat);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(famosoRepository.save(famoso));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de un famoso (Solo ADMIN)")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Famoso datos) {
        return famosoRepository.findById(id).map(existente -> {
            existente.setNombre(datos.getNombre());
            existente.setDescripcion(datos.getDescripcion());
            existente.setPrecio(datos.getPrecio());
            existente.setTiempoEntregaDias(datos.getTiempoEntregaDias());
            existente.setActivo(datos.getActivo());
            if (datos.getCategoria() != null && datos.getCategoria().getId() != null) {
                Categoria cat = categoriaRepository.findById(datos.getCategoria().getId())
                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                existente.setCategoria(cat);
            }
            return ResponseEntity.ok(famosoRepository.save(existente));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar/Eliminar un famoso (Solo ADMIN)")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        return famosoRepository.findById(id).map(famoso -> {
            famoso.setActivo(false);
            famosoRepository.save(famoso);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // --- Endpoints Multimedia por Famoso (Solo ADMIN) ---

    @PostMapping("/{id}/fotos")
    @Operation(summary = "Agregar foto (URL ImgBB) a un famoso (Solo ADMIN)")
    public ResponseEntity<?> agregarFoto(@PathVariable Long id, @Valid @RequestBody FotoFamoso foto) {
        Famoso famoso = famosoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Famoso no encontrado"));
        foto.setFamoso(famoso);
        return ResponseEntity.status(HttpStatus.CREATED).body(fotoRepository.save(foto));
    }

    @PostMapping("/{id}/videos")
    @Operation(summary = "Agregar video de muestra a un famoso (Solo ADMIN)")
    public ResponseEntity<?> agregarVideo(@PathVariable Long id, @Valid @RequestBody VideoFamoso video) {
        Famoso famoso = famosoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Famoso no encontrado"));
        video.setFamoso(famoso);
        return ResponseEntity.status(HttpStatus.CREATED).body(videoRepository.save(video));
    }

    @PostMapping("/{id}/audios")
    @Operation(summary = "Agregar audio de muestra a un famoso (Solo ADMIN)")
    public ResponseEntity<?> agregarAudio(@PathVariable Long id, @Valid @RequestBody AudioFamoso audio) {
        Famoso famoso = famosoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Famoso no encontrado"));
        audio.setFamoso(famoso);
        return ResponseEntity.status(HttpStatus.CREATED).body(audioRepository.save(audio));
    }
}