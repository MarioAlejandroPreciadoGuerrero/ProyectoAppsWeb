/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import Enum.EstadoReservacion;
import java.sql.Time;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author USER
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Horario {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idHorario;
  
    @Column
    private Time horaInicio;
    
    @Enumerated(EnumType.STRING)
    private EstadoReservacion estado;
    
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "idServicio" , nullable = false)
    private Servicio idServicio;
}
