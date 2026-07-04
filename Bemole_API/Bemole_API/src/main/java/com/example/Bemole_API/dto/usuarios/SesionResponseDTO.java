package com.example.Bemole_API.dto.usuarios;

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
    private UsuarioResumenDTO usuario;
}
