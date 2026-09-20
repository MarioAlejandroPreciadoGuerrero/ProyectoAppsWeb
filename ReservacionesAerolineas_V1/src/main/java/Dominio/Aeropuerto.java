/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

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
public class Aeropuerto {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idAeropuerto;
    
    @Column
    private String nombre;
    
    @Column 
    private String codigoIata;
}
