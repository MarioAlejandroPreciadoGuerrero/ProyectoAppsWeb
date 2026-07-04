package com.example.Bemole_API.models;
import jakarta.persistence.*;

@Entity
@Table(
        name = "items_carrito",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_carrito_producto",
                        columnNames = {
                                "carrito_id",
                                "producto_id"
                        }
                )
        }
)
public class ItemCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "carrito_id",
            nullable = false
    )
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "producto_id",
            nullable = false
    )
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    public ItemCarrito() {
    }

    public ItemCarrito(Carrito carrito, Producto producto, Integer cantidad) {
        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public ItemCarrito(Long id, Carrito carrito, Producto producto, Integer cantidad) {
        this.id = id;
        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "ItemCarrito{" + "id=" + id + ", carrito=" + carrito + ", producto=" + producto + ", cantidad=" + cantidad + '}';
    }
}
