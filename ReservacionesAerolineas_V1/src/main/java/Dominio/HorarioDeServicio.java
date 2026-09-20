/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

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
public class HorarioDeServicio {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idHorarioDeServicio;

    @Column
    private int dia;
    
    @Column
    private Time horaDeIncio;
    
    @Column
    private Time horaFin;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "idServicio" , nullable = false)
    private Servicio idServicio;
    
}
