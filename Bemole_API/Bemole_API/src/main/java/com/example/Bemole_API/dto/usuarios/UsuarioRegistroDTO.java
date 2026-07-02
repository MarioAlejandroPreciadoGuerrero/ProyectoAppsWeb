package com.example.Bemole_API.dto.usuarios;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRegistroDTO {
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(
            max = 100,
            message = "El nombre no puede superar los 100 caracteres."
    )
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio.")
    @Size(
            max = 100,
            message = "El apellido no puede superar los 100 caracteres."
    )
    private String apellido;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El formato del correo electrónico no es válido.")
    @Size(
            max = 255,
            message = "El correo electrónico no puede superar los 255 caracteres."
    )
    private String email;

    @Pattern(
            regexp = "^$|^[0-9+()\\-\\s]{7,20}$",
            message = "El teléfono no tiene un formato válido."
    )
    private String telefono;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres e incluir una mayúscula, una minúscula, un número y un símbolo."
    )
    private String password;

    @NotBlank(message = "La confirmación de contraseña es obligatoria.")
    private String confirmarPassword;

    @AssertTrue(message = "Debes aceptar los términos y condiciones.")
    private boolean aceptaTerminos;
}
