package com.example.E_commerce_Bemole.modules;

import com.example.E_commerce_Bemole.enums.EstadoPago;
import com.example.E_commerce_Bemole.enums.MetodoPago;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    @Column(nullable = false)
    private Double monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estadoPago;

    @Column(nullable = false)
    private LocalDateTime fecha;

    public Pago() {
    }

    public Pago(Orden orden, Double monto, MetodoPago metodoPago,
                EstadoPago estadoPago, LocalDateTime fecha) {
        this.orden = orden;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.estadoPago = estadoPago;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public Orden getOrden() {
        return orden;
    }

    public Double getMonto() {
        return monto;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Pago{id=" + id + ", monto=" + monto +
                ", metodo=" + metodoPago + ", estado=" + estadoPago + "}";
    }
}