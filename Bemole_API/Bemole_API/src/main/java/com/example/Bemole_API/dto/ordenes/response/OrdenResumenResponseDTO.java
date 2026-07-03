package com.example.Bemole_API.dto.ordenes.response;

import com.example.Bemole_API.enums.EstadoOrden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenResumenResponseDTO {
    private Long id;
    private String numero;
    private LocalDateTime fecha;
    private EstadoOrden estado;
    private BigDecimal total;
    private Integer cantidadProductos;
    private String productoPrincipal;
    private Integer productosAdicionales;
}
