package com.example.E_commerce_Bemole.dto.ordenes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DireccionOrdenFormDTO {
    @NotBlank(message = "La calle y el número son obligatorios.")
    @Size(max = 255, message = "La calle y el número no pueden superar los 255 caracteres.")
    private String calleNumero;

    @NotBlank(message = "La colonia es obligatoria.")
    @Size(max = 150, message = "La colonia no puede superar los 150 caracteres.")
    private String colonia;

    @NotBlank(message = "El código postal es obligatorio.")
    @Pattern(regexp = "^[0-9]{5}$", message = "El código postal debe contener 5 dígitos.")
    private String codigoPostal;

    @NotBlank(message = "La ciudad es obligatoria.")
    @Size(max = 100, message = "La ciudad no puede superar los 100 caracteres.")
    private String ciudad;

    @NotBlank(message = "El estado es obligatorio.")
    @Size(max = 100, message = "El estado no puede superar los 100 caracteres.")
    private String estado;
}
