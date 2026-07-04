package com.example.Bemole_API.dto.ordenes.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactoOrdenResponseDTO {
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
}
