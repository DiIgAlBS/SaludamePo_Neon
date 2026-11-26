package com.duoc.saludamepo_neon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "fotos_famosos")
public class FotoFamoso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String url;

    @Column(length = 200)
    private String descripcion;

    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal = false;

    @ManyToOne
    @JoinColumn(name = "famoso_id", nullable = false)
    @JsonIgnore
    private Famoso famoso;
}