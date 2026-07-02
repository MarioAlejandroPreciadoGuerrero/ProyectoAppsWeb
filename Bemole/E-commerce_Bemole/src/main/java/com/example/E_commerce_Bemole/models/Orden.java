package com.example.E_commerce_Bemole.models;
import com.example.E_commerce_Bemole.models.Pago;

import com.example.E_commerce_Bemole.enums.EstadoOrden;
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

    @Column(nullable = false)
    private String direccionEnvio;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOrden> items;

    @OneToOne(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pago pago;

    public Orden() {
    }

    public Orden(Usuario usuario, String numero, LocalDateTime fecha,
                 EstadoOrden estado, Double total, String direccionEnvio) {
        this.usuario = usuario;
        this.numero = numero;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.direccionEnvio = direccionEnvio;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getNumero() {
        return numero;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public Double getTotal() {
        return total;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public List<ItemOrden> getItems() {
        return items;
    }

    public Pago getPago() {
        return pago;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public void setItems(List<ItemOrden> items) {
        this.items = items;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    @Override
    public String toString() {
        return "Orden{id=" + id + ", numero=" + numero +
                ", usuario=" + usuario.getNombre() +
                ", estado=" + estado + ", total=" + total + "}";
    }
}