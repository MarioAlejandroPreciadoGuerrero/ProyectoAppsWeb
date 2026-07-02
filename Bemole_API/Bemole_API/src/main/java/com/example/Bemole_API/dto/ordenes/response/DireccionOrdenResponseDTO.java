package com.example.Bemole_API.dto.ordenes.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DireccionOrdenResponseDTO {
    private String calleNumero;
    private String colonia;
    private String codigoPostal;
    private String ciudad;
    private String estado;
}
