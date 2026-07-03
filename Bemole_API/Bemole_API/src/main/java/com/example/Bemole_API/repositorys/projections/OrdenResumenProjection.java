package com.example.Bemole_API.repositorys.projections;

import com.example.Bemole_API.enums.EstadoOrden;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrdenResumenProjection {
    Long getId();

    String getNumero();

    LocalDateTime getFecha();

    EstadoOrden getEstado();

    BigDecimal getTotal();

    Long getCantidadProductos();
}
