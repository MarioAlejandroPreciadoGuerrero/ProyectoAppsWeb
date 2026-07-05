package com.example.Bemole_Dashboard_Admin.dto.admin.orden;

import com.example.Bemole_Dashboard_Admin.enums.EstadoOrden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarEstadoOrdenFormDTO {
    @NotNull(message = "El nuevo estado es obligatorio.")
    private EstadoOrden estado;
}
