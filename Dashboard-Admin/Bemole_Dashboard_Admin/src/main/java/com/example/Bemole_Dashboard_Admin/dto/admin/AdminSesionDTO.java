package com.example.Bemole_Dashboard_Admin.dto.admin;

import java.io.Serializable;

public class AdminSesionDTO implements Serializable {
    //No hay lombok por seguridad en los metodos

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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
