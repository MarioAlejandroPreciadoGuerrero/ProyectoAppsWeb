package com.example.Bemole_API.dto.pago;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearPagoResponseDTO {
    private Long ordenId;
    private String preferenceId;
    private String initPoint;


}
