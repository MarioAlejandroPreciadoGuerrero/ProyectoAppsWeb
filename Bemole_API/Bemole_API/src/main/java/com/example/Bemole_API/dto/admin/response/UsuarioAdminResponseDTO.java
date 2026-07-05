package com.example.Bemole_API.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
/*DTO para consultar usuarios no tiene nada que ver con el login*/
public class UsuarioAdminResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private LocalDateTime fechaRegistro;
}
