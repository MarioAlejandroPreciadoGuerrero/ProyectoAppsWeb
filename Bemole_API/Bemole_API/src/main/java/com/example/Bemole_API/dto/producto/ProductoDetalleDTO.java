package com.example.Bemole_API.dto.producto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDetalleDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private boolean activo;
    private boolean disponible;
    private CategoriaProductoDTO categoria;
}
