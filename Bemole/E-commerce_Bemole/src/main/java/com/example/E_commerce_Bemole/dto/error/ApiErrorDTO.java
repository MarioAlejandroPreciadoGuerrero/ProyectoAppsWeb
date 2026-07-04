package com.example.E_commerce_Bemole.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorDTO {
    private Instant timestamp;
    private int status;
    private String codigo;
    private String mensaje;
    private String ruta;
    private Map<String, String> campos;
}
