package com.example.Bemole_API.dto.carrito;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCarritoResponseDTO {
    private Long id;
    private Long productoId;
    private String nombreProducto;
    private String categoria;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private Integer stockDisponible;
    private BigDecimal subtotal;
    private String imagenUrl;
}
