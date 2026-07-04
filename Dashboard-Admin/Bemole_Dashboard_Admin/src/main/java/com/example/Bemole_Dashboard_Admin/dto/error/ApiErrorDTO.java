package com.example.Bemole_Dashboard_Admin.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorDTO {
    private LocalDateTime fecha;
    private Integer status;
    private String codigo;
    private String mensaje;
    private String ruta;
    private Map<String, String> errores;
}
