package com.example.Bemole_Dashboard_Admin.dto.admin.orden;

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
