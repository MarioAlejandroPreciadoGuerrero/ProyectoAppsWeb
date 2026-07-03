package com.example.E_commerce_Bemole.dto.usuarios;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSesionDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
}
