package com.example.Bemole_API.models;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(
        name = "items_orden",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_orden_producto",
                        columnNames = {
                                "orden_id",
                                "producto_id"
                        }
                )
        }
)
@Data
@NoArgsConstructor
public class ItemOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(
            name = "precio_unitario",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal precioUnitario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "orden_id",
            nullable = false
    )
    private Orden orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "producto_id",
            nullable = false
    )
    private Producto producto;


}
