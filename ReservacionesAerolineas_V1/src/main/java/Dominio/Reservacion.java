/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import Enum.EstadoReservacion;
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
public class Reservacion {
    
    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY)
    private Long idReservacion;
    
    @Enumerated(EnumType.STRING)
    private EstadoReservacion estado;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHorario", nullable = false, unique = true)
    private Horario idHorario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCliente" , nullable = false)
    private Cliente idCliente;
}
