package com.duoc.saludamepo_neon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "audios_famosos")
public class AudioFamoso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String url;

    @Column(length = 150)
    private String titulo;

    @Column(name = "duracion_segundos")
    private Integer duracionSegundos;

    @ManyToOne
    @JoinColumn(name = "famoso_id", nullable = false)
    @JsonIgnore
    private Famoso famoso;
}