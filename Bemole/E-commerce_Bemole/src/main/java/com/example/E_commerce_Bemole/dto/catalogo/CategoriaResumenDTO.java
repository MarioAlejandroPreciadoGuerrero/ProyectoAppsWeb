package com.example.E_commerce_Bemole.dto.catalogo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaResumenDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private long cantidadProductos;
}
