package com.duoc.saludamepo_neon.controller;

import com.duoc.saludamepo_neon.model.Blog;
import com.duoc.saludamepo_neon.model.MensajeContacto;
import com.duoc.saludamepo_neon.repository.BlogRepository;
import com.duoc.saludamepo_neon.repository.MensajeContactoRepository;
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
@Tag(name = "5. Blogs y Contacto", description = "Noticias Pop y mensajes del formulario de contacto")
public class BlogContactoController {

    private final BlogRepository blogRepository;
    private final MensajeContactoRepository contactoRepository;

    // --- BLOGS ---
    @GetMapping("/blogs")
    @Operation(summary = "Listar todas las noticias del blog (Público)")
    public List<Blog> listarBlogs() {
        return blogRepository.findAllByOrderByFechaPublicacionDesc();
    }

    @PostMapping("/blogs")
    @Operation(summary = "Publicar nueva noticia en el blog (Solo ADMIN)")
    public ResponseEntity<?> crearBlog(@Valid @RequestBody Blog blog) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogRepository.save(blog));
    }

    @PutMapping("/blogs/{id}")
    @Operation(summary = "Actualizar noticia del blog (Solo ADMIN)")
    public ResponseEntity<?> actualizarBlog(@PathVariable Long id, @Valid @RequestBody Blog datos) {
        return blogRepository.findById(id).map(existente -> {
            existente.setTitulo(datos.getTitulo());
            existente.setCategoria(datos.getCategoria());
            existente.setResumen(datos.getResumen());
            existente.setContenido(datos.getContenido());
            existente.setImagenUrl(datos.getImagenUrl());
            return ResponseEntity.ok(blogRepository.save(existente));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/blogs/{id}")
    @Operation(summary = "Eliminar noticia del blog (Solo ADMIN)")
    public ResponseEntity<?> eliminarBlog(@PathVariable Long id) {
        if (!blogRepository.existsById(id)) return ResponseEntity.notFound().build();
        blogRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- CONTACTO ---
    @PostMapping("/contacto")
    @Operation(summary = "Enviar mensaje de contacto desde la web (Público)")
    public ResponseEntity<?> enviarMensaje(@Valid @RequestBody MensajeContacto mensaje) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactoRepository.save(mensaje));
    }

    @GetMapping("/contacto")
    @Operation(summary = "Listar mensajes recibidos (Solo ADMIN)")
    public List<MensajeContacto> listarMensajes() {
        return contactoRepository.findAllByOrderByFechaEnvioDesc();
    }

    @PutMapping("/contacto/{id}/leido")
    @Operation(summary = "Marcar mensaje de contacto como leído (Solo ADMIN)")
    public ResponseEntity<?> marcarLeido(@PathVariable Long id) {
        return contactoRepository.findById(id).map(msg -> {
            msg.setLeido(true);
            return ResponseEntity.ok(contactoRepository.save(msg));
        }).orElse(ResponseEntity.notFound().build());
    }
}