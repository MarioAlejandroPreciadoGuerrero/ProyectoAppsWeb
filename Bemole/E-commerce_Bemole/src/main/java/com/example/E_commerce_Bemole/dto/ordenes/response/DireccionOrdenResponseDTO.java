package com.example.E_commerce_Bemole.dto.ordenes.response;

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
