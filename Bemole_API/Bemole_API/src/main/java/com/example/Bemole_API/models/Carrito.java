package com.example.Bemole_API.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "carritos")
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrito> items;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    public Carrito() {
    }

    public Carrito(Usuario usuario, List<ItemCarrito> items, LocalDateTime fechaCreacion) {
        this.usuario = usuario;
        this.items = items;
        this.fechaCreacion = fechaCreacion;
    }

    public Carrito(Long id, Usuario usuario, List<ItemCarrito> items, LocalDateTime fechaCreacion) {
        this.id = id;
        this.usuario = usuario;
        this.items = items;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    public void setItems(List<ItemCarrito> items) {
        this.items = items;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "Carrito{" + "id=" + id + ", usuario=" + usuario + ", items=" + items + ", fechaCreacion=" + fechaCreacion + '}';
    }
}
