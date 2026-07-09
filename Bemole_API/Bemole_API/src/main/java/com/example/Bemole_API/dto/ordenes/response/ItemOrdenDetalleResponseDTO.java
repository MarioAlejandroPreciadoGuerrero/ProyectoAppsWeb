package com.example.Bemole_API.dto.ordenes.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrdenDetalleResponseDTO {
    private Long productoId;
    private String nombreProducto;
    private String categoria;
    private String imagenUrl;

    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal subtotal;
}
