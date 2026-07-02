package com.example.Bemole_API.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "direcciones_orden",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_direccion_orden",
                        columnNames = "orden_id"
                )
        }
)
@Data
@NoArgsConstructor
public class DireccionOrden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "orden_id",
            nullable = false,
            unique = true
    )
    private Orden orden;

    @Column(
            name = "calle_numero",
            nullable = false,
            length = 255
    )
    private String calleNumero;

    @Column(
            nullable = false,
            length = 150
    )
    private String colonia;

    @Column(
            name = "codigo_postal",
            nullable = false,
            length = 10
    )
    private String codigoPostal;

    @Column(
            nullable = false,
            length = 100
    )
    private String ciudad;

    @Column(
            nullable = false,
            length = 100
    )
    private String estado;
}
