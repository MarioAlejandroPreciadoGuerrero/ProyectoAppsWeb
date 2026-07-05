package com.example.Bemole_Dashboard_Admin.dto.admin.producto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarActivoProductoDTO {
    @NotNull(message = "El estado activo es obligatorio.")
    private Boolean activo;
}
