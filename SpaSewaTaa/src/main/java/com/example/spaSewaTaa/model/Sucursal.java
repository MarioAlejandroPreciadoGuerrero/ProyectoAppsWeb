package com.example.spaSewaTaa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sucursales")
public class Sucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_sucursal;

    @Column
    private String direccion;

    @Column
    private String colonia;

    @Column
    private String codigo_postal;

    @Column
    private String telefono;

    @Column
    private LocalTime hora_apertura;

    @Column
    private LocalTime hora_cierre;
}
