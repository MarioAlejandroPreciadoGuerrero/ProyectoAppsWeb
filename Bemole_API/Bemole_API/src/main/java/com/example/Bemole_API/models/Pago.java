package com.example.Bemole_API.models;

import com.example.Bemole_API.enums.EstadoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false, unique = true)
    private Orden orden;

    @Column(unique = true)
    private String mercadoPagoPaymentId;

    private String preferenceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPago estado;

    private String metodoPago;

    private BigDecimal monto;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

}
