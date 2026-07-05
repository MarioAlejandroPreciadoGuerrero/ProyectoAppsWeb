package com.example.Bemole_API.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminResumenDTO {
    private  long totalProductos;
    private long totalCategorias;
    private long totalUsuarios;
    private long totalOrdenes;
    private long ordenesPendientes;
    private BigDecimal ventasTotales;
}

