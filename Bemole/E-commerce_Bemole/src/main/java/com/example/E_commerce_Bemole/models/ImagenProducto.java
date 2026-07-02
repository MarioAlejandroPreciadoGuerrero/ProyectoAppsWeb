package com.example.E_commerce_Bemole.modules;

import com.example.E_commerce_Bemole.models.Producto;
import jakarta.persistence.*;

@Entity
@Table(name = "imagenes_producto")
public class ImagenProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public ImagenProducto() {
    }

    public ImagenProducto(String url, Producto producto) {
        this.url = url;
        this.producto = producto;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public String toString() {
        return "ImagenProducto{id=" + id + ", url=" + url + "}";
    }
}