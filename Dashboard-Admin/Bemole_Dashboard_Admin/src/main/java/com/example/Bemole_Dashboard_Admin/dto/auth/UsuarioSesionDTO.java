package com.example.Bemole_Dashboard_Admin.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioSesionDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;

    public boolean esAdministrador() {
        if (rol == null || rol.isBlank()) {
            return false;
        }

        return rol.equalsIgnoreCase("ADMINISTRADOR")
                || rol.equalsIgnoreCase("ROLE_ADMINISTRADOR")
                || rol.equalsIgnoreCase("ADMIN")
                || rol.equalsIgnoreCase("ROLE_ADMIN");
    }

    public String getNombreCompleto() {
        String nombreSeguro = nombre != null ? nombre.trim() : "";

        String apellidoSeguro = apellido != null ? apellido.trim() : "";

        return (nombreSeguro + " " + apellidoSeguro).trim();
    }

    public String getInicial() {
        if (nombre == null || nombre.isBlank()) {
            return "A";
        }

        return nombre.substring(0, 1).toUpperCase();
    }
}
