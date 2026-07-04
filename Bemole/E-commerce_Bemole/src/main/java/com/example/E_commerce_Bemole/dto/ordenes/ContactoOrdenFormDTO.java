package com.example.E_commerce_Bemole.dto.ordenes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactoOrdenFormDTO {
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio.")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres.")
    private String apellido;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "El correo no tiene un formato válido.")
    @Size(max = 255, message = "El correo no puede superar los 255 caracteres.")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio.")
    @Pattern(regexp = "^[0-9+()\\-\\s]{7,20}$", message = "El teléfono no tiene un formato válido.")
    private String telefono;
}
