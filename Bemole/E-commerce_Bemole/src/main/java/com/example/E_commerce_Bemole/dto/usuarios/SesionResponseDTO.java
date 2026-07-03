package com.example.E_commerce_Bemole.dto.usuarios;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SesionResponseDTO {
    private String token;
    private String tipoToken;
    private long expiraEn;
    private UsuarioSesionDTO usuario;
}
