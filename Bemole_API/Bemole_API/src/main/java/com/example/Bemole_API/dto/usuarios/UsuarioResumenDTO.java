package com.example.Bemole_API.dto.usuarios;

import com.example.Bemole_API.enums.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResumenDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private RolUsuario rol;
}
