package com.example.E_commerce_Bemole.dto.ordenes;

import com.example.E_commerce_Bemole.enums.MetodoEnvio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearOrdenFormDTO {
    @Valid
    @NotNull(message = "Los datos de contacto son obligatorios.")
    private ContactoOrdenFormDTO contacto = new ContactoOrdenFormDTO();

    @Valid
    @NotNull(message = "La dirección es obligatoria.")
    private DireccionOrdenFormDTO direccionEnvio = new DireccionOrdenFormDTO();

    @NotNull(message = "Selecciona un método de envío.")
    private MetodoEnvio metodoEnvio;
}
