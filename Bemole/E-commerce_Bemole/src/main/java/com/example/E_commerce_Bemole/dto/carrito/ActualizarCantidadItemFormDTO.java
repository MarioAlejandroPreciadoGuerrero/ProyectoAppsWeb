package com.example.E_commerce_Bemole.dto.carrito;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarCantidadItemFormDTO {
    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 1, message = "La cantidad mínima es 1.")
    @Max(value = 99, message = "La cantidad máxima es 99.")
    private Integer cantidad;
}
