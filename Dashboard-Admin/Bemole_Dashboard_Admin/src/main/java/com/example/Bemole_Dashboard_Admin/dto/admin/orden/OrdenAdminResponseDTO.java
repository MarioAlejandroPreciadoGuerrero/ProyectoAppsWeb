package com.example.Bemole_Dashboard_Admin.dto.admin.orden;

import com.example.Bemole_Dashboard_Admin.enums.EstadoOrden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenAdminResponseDTO {
    private Long id;
    private String numero;
    private LocalDateTime fecha;
    private EstadoOrden estado;
    private BigDecimal total;
    private Integer cantidadProductos;
    private String productoPrincipal;
    private Integer productosAdicionales;

    public String getEstadoTexto() {
        if (estado == null) {
            return "Sin estado";
        }

        return switch (estado) {
            case PENDIENTE -> "Pendiente";
            case PROCESANDO -> "Procesando";
            case ENVIADO -> "Enviada";
            case ENTREGADO -> "Entregada";
            case CANCELADA -> "Cancelada";
        };
    }

    public boolean permiteProcesar() {
        return estado == EstadoOrden.PENDIENTE;
    }

    public boolean permiteEnviar() {
        return estado == EstadoOrden.PROCESANDO;
    }

    public boolean permiteEntregar() {
        return estado == EstadoOrden.ENVIADO;
    }

    public boolean permiteCancelar() {
        return estado == EstadoOrden.PENDIENTE
                || estado == EstadoOrden.PROCESANDO;
    }
}
