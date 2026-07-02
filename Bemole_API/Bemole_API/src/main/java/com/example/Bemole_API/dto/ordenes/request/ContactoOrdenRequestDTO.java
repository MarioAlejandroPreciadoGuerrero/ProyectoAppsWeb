package com.example.Bemole_API.dto.ordenes.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactoOrdenRequestDTO {

    @NotBlank(message = "El nombre de contacto es obligatorio.")
    @Size(
            max = 100,
            message = "El nombre no puede superar los 100 caracteres."
    )
    private String nombre;

    @NotBlank(message = "El apellido de contacto es obligatorio.")
    @Size(
            max = 100,
            message = "El apellido no puede superar los 100 caracteres."
    )
    private String apellido;

    @NotBlank(message = "El correo de contacto es obligatorio.")
    @Email(message = "El correo de contacto no es válido.")
    @Size(
            max = 255,
            message = "El correo no puede superar los 255 caracteres."
    )
    private String email;

    @NotBlank(message = "El teléfono de contacto es obligatorio.")
    @Pattern(
            regexp = "^[0-9+()\\-\\s]{7,20}$",
            message = "El teléfono de contacto no es válido."
    )
    private String telefono;
}