/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.sql.Time;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Mario Alejandro Preciado Guerrero
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servicio {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idServicio;
    
    @Column 
    private String nombre;
    
    @Column 
    private String descripcion;
    
    @Column
    private Integer duracion;
    
    @Column
    private double costo;
    
    @OneToMany (mappedBy = "idServicio", cascade = CascadeType.ALL)
    private List<HorarioDeServicio> horarioDeServicio;
    
    @OneToMany (mappedBy = "idServicio", cascade = CascadeType.ALL)
    private List<Horario> Horarios;
}
