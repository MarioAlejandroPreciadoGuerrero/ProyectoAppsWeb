package com.example.E_commerce_Bemole.dto.usuarios;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRegistroDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String password;
    private String rol;
}
