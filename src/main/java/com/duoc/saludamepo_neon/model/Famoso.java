package com.duoc.saludamepo_neon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "famosos")
public class Famoso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String nombre;

    @NotBlank @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String descripcion;

    @NotNull @Min(0)
    @Column(nullable = false)
    private Integer precio;

    @Column(name = "tiempo_entrega_dias", nullable = false)
    private Integer tiempoEntregaDias = 3;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "famoso", cascade = CascadeType.ALL)
    private List<FotoFamoso> fotos;

    @OneToMany(mappedBy = "famoso", cascade = CascadeType.ALL)
    private List<VideoFamoso> videos;

    @OneToMany(mappedBy = "famoso", cascade = CascadeType.ALL)
    private List<AudioFamoso> audios;
}