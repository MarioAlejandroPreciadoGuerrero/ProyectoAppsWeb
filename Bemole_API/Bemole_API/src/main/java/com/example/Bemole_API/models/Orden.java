package com.example.Bemole_API.models;
import com.example.Bemole_API.enums.EstadoOrden;
import com.example.Bemole_API.enums.MetodoEnvio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "ordenes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_orden_numero",
                        columnNames = "numero"
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "usuario_id",
            nullable = false
    )
    private Usuario usuario;

    @Column(
            nullable = false,
            unique = true,
            length = 255
    )
    private String numero;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal total;;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOrden> items = new ArrayList<>();;

    @Column(
            name = "contacto_nombre",
            length = 100
    )
    private String contactoNombre;

    @Column(
            name = "contacto_apellido",
            length = 100
    )
    private String contactoApellido;

    @Column(
            name = "contacto_email",
            length = 255
    )
    private String contactoEmail;

    @Column(
            name = "contacto_telefono",
            length = 25
    )
    private String contactoTelefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_envio")
    private MetodoEnvio metodoEnvio;

    @Column(
            name = "costo_envio",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal costoEnvio = BigDecimal.ZERO;

    @OneToOne(
            mappedBy = "orden",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private DireccionOrden direccionEnvio;

    public void asignarDireccion(DireccionOrden direccion) {
        this.direccionEnvio = direccion;

        if (direccion != null) {
            direccion.setOrden(this);
        }
    }

    public void agregarItem(ItemOrden item) {
        items.add(item);
        item.setOrden(this);
    }

    public void eliminarItem(ItemOrden item) {
        items.remove(item);
        item.setOrden(null);
    }
}