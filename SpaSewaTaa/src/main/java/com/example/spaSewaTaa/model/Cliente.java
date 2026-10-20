package com.example.spaSewaTaa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cliente;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String contrasenia;

    @Column
    private String telefono;

    @Column
    private LocalDateTime fecha_registro;

    @Column(nullable = false)
    LocalDate fecha_nacimiento;

    @Column
    private String notas_medicas;

    @Column
    private boolean activo;
}
