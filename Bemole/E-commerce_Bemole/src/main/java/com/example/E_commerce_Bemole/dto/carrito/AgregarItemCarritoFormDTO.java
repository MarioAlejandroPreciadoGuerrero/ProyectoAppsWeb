package com.example.E_commerce_Bemole.dto.carrito;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgregarItemCarritoFormDTO {
    @NotNull(message = "El producto es obligatorio.")
    @Positive(message = "El producto seleccionado no es válido.")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 1, message = "La cantidad mínima es 1.")
    @Max(value = 99, message = "La cantidad máxima es 99.")
    private Integer cantidad;
}
