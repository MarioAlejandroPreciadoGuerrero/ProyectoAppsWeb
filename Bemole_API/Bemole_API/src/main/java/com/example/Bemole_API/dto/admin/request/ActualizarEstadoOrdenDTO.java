package com.example.Bemole_API.dto.admin.request;

import com.example.Bemole_API.enums.EstadoOrden;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarEstadoOrdenDTO {
    @NotNull(message = "El nuevo estado es obligatorio.")
    private EstadoOrden estado;
}
