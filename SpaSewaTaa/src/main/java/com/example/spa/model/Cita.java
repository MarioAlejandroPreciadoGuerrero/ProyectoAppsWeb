package com.example.spa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "citas")
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cita;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false)
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "id_terapeuta", nullable = false)
    private Terapeuta terapeuta;

    @ManyToOne
    @JoinColumn(name = "id_sala", nullable = false)
    private Sala sala;

    @Column
    private LocalDateTime fecha_hora_inicio;

    @Column
    private LocalDateTime fecha_hora_fin;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoCita estado;

    @Column
    private LocalDateTime fecha_creada;

    @Column(precision = 10, scale = 2)
    private BigDecimal precio_total;
}
