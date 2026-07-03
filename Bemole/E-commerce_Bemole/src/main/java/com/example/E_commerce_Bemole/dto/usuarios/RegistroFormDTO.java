package com.example.E_commerce_Bemole.dto.usuarios;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroFormDTO {
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio.")
    @Size(max = 100)
    private String apellido;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "El correo no tiene un formato válido.")
    private String email;

    @Pattern(
            regexp = "^$|^[0-9+()\\-\\s]{7,20}$",
            message = "El teléfono no tiene un formato válido."
    )
    private String telefono;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;

    @NotBlank(message = "Debes confirmar la contraseña.")
    private String confirmarPassword;

    @AssertTrue(message = "Debes aceptar los términos y condiciones.")
    private boolean aceptaTerminos;
}
