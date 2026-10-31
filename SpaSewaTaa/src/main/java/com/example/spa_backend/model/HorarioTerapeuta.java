package com.example.spa_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "horario_terapeuta")
public class HorarioTerapeuta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_horario_terapeuta;

    @JoinColumn
    private Sucursal sucursal;

    @Column(nullable = false)
    private String dias_trabajo;

    @Column
    private LocalTime hora_inicio;

    @Column
    private LocalTime hora_fin;


}
