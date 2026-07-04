package com.example.Bemole_Dashboard_Admin.dto.auth.response;

import com.example.Bemole_Dashboard_Admin.dto.auth.UsuarioSesionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SesionResponseDTO {
    private String token;
    private UsuarioSesionDTO usuario;
}
