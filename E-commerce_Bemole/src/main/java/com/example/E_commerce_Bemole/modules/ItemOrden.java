package com.example.E_commerce_Bemole.modules;

import jakarta.persistence.*;

@Entity
@Table(name = "items_orden")
public class ItemOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioUnitario;

    @Column(nullable = false)
    private Double descuento;

    public ItemOrden() {
    }

    public ItemOrden(Orden orden, Producto producto, Integer cantidad,
                     Double precioUnitario, Double descuento) {
        this.orden = orden;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento;
    }

    public Long getId() {
        return id;
    }

    public Orden getOrden() {
        return orden;
    }

    public Producto getProducto() {
        return producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    @Override
    public String toString() {
        return "ItemOrden{id=" + id + ", producto=" + producto.getNombre() +
                ", cantidad=" + cantidad + ", precioUnitario=" + precioUnitario + "}";
    }
}