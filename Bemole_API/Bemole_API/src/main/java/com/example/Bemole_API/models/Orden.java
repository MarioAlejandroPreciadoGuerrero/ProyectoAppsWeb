package com.example.Bemole_API.models;
import com.example.Bemole_API.enums.EstadoOrden;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ordenes")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado;

    @Column(nullable = false)
    private Double total;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOrden> items;

    public Orden() {
    }

    public Orden(Usuario usuario, String numero, LocalDateTime fecha, EstadoOrden estado, Double total) {
        this.usuario = usuario;
        this.numero = numero;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
    }

    public Orden(Long id, Usuario usuario, String numero, LocalDateTime fecha, EstadoOrden estado, Double total, List<ItemOrden> items) {
        this.id = id;
        this.usuario = usuario;
        this.numero = numero;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.items = items;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<ItemOrden> getItems() {
        return items;
    }

    public void setItems(List<ItemOrden> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Orden{id=" + id + ", numero=" + numero + ", usuario=" + usuario.getNombre() + ", estado=" + estado + ", total=" + total + "}";
    }
}