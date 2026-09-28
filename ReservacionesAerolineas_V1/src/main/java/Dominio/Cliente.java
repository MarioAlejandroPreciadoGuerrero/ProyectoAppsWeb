/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.List;
import javax.persistence.*;

/**
 *
 * @author USER
 */
@Entity
public class Cliente {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idCliente;
    
    @Column
    private String nombre;
    
    @Column 
    private String correo;
    
    @Column
    private String contraseña;
    
    @Column
    private String numeroTelefono;
    
    @OneToMany(mappedBy = "idCliente")
    private List<Reservacion> listaReservaciones;

    public Cliente() {
    }

    public Cliente( String nombre, String correo, String contraseña, String numeroTelefono, List<Reservacion> listaReservaciones) {
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.numeroTelefono = numeroTelefono;
        this.listaReservaciones = listaReservaciones;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public List<Reservacion> getListaReservaciones() {
        return listaReservaciones;
    }

    public void setListaReservaciones(List<Reservacion> listaReservaciones) {
        this.listaReservaciones = listaReservaciones;
    }
    
    
    
}
