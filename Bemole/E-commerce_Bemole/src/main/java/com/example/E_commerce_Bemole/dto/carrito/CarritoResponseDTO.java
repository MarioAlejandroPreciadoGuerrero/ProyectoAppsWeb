package com.example.E_commerce_Bemole.dto.carrito;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoResponseDTO {
    private Long id;
    private List<ItemCarritoResponseDTO> items = new ArrayList<>();
    private Integer cantidadItems;
    private Integer cantidadProductos;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
}
