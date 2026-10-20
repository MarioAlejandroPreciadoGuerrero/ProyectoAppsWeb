package com.example.spaSewaTaa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="terapeutas_servicios")
public class TerapeutasServicios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_terapeutas_servicios;

    //agregar llaves foraneas de terapeuta y servicios para definir que servicios da un terapeuta

}
