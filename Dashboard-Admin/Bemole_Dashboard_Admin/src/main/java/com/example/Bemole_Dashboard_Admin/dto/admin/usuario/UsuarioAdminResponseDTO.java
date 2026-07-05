package com.example.Bemole_Dashboard_Admin.dto.admin.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioAdminResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private LocalDateTime fechaRegistro;

    public String getNombreCompleto() {
        String nombreSeguro = nombre != null ? nombre : "";

        String apellidoSeguro = apellido != null ? apellido : "";

        return (nombreSeguro + " " + apellidoSeguro).trim();
    }
}
