package com.example.spa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_servicio;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Integer minutos_duracion;

    @Column
    private String descripcion;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column
    private String url_imagen;

    @ManyToMany(mappedBy = "servicios")
    private List<Terapeuta> terapeutas;
}
