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
@Builder
@Entity
@Table(name = "pagos")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pago;

    @OneToOne
    @JoinColumn(name = "id_cita", nullable = false)
    private Cita cita;

    @ManyToOne
    @JoinColumn(name = "id_metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    @Column(precision = 10, scale = 2)
    private BigDecimal monto;

    @Column
    private LocalDateTime fecha_pago;

    @Column
    @Enumerated(EnumType.STRING)
    private EstadoPago estado_pago;
}
