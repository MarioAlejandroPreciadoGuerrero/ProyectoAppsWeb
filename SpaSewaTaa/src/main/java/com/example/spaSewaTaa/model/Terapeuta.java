package com.example.spaSewaTaa.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "terapeutas")
public class Terapeuta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_terapeuta;

    @Column(nullable = false)
    private String nombre;

//    @OneToOne
//    @ForeignKey()
//    private Long id_sucursal;

    @Column
    private LocalDate fecha_contratacion;

    @Column
    private boolean activo;

}
