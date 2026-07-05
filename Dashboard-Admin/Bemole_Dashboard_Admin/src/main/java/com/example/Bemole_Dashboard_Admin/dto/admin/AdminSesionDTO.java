package com.example.Bemole_Dashboard_Admin.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminSesionDTO implements Serializable {


    private Long usuarioId;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private String token;

    public String getNombreCompleto() {
        String nombreSeguro =
                nombre != null ? nombre.trim() : "";

        String apellidoSeguro =
                apellido != null ? apellido.trim() : "";

        return (nombreSeguro + " " + apellidoSeguro).trim();
    }

    public String getInicial() {
        if (nombre == null || nombre.isBlank()) {
            return "A";
        }

        return nombre.substring(0, 1).toUpperCase();
    }


}
