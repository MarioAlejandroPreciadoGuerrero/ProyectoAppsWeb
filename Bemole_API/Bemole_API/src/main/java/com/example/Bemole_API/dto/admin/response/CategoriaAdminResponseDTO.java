package com.example.Bemole_API.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaAdminResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private long cantidadProductos;
}
