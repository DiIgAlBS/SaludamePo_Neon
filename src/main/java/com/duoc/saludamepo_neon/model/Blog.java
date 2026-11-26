package com.duoc.saludamepo_neon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 200)
    private String titulo;

    @NotBlank
    @Column(nullable = false, length = 80)
    private String categoria;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String resumen;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(name = "fecha_publicacion", nullable = false)
    private LocalDate fechaPublicacion = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Usuario autor;
}