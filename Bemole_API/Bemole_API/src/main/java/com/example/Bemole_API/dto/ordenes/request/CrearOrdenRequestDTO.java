package com.example.Bemole_API.dto.ordenes.request;

import com.example.Bemole_API.enums.MetodoEnvio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearOrdenRequestDTO {

    @Valid
    @NotNull(message = "Los datos de contacto son obligatorios.")
    private ContactoOrdenRequestDTO contacto;

    @Valid
    @NotNull(message = "La dirección de envío es obligatoria.")
    private DireccionOrdenRequestDTO direccionEnvio;

    @NotNull(message = "El método de envío es obligatorio.")
    private MetodoEnvio metodoEnvio;
}
