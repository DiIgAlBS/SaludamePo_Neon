package com.duoc.saludamepo_neon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonIgnore
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "famoso_id", nullable = false)
    private Famoso famoso;

    @NotNull @Min(1)
    @Column(nullable = false)
    private Integer cantidad = 1;

    @NotNull @Min(0)
    @Column(name = "precio_unitario", nullable = false)
    private Integer precioUnitario;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String destinatario;

    @Column(columnDefinition = "TEXT")
    private String dedicatoria;
}