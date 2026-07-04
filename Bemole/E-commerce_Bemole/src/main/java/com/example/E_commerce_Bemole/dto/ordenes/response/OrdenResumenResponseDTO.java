package com.example.E_commerce_Bemole.dto.ordenes.response;

import com.example.E_commerce_Bemole.enums.EstadoOrden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    public String getFechaTexto() {
        if (fecha == null) {
            return "Fecha no disponible";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "MX"));

        return fecha.format(formatter);
    }

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

    public String getEstadoIcono() {
        if (estado == null) {
            return "•";
        }

        return switch (estado) {
            case PENDIENTE -> "🕒";
            case PROCESANDO -> "⚙️";
            case ENVIADO -> "🚚";
            case ENTREGADO -> "✓";
            case CANCELADA -> "✕";
        };
    }

    public String getEstadoCss() {
        if (estado == null) {
            return "";
        }

        return switch (estado) {
            case PENDIENTE ->
                    "status-badge--pending";

            case PROCESANDO ->
                    "status-badge--processing";

            case ENVIADO ->
                    "status-badge--transit";

            case ENTREGADO ->
                    "status-badge--delivered";

            case CANCELADA ->
                    "status-badge--cancelled";
        };
    }

    public String getResumenProductos() {
        if (productoPrincipal == null || productoPrincipal.isBlank()) {
            return "Productos de la orden";
        }

        if (productosAdicionales != null
                && productosAdicionales > 0) {
            return productoPrincipal
                    + " + "
                    + productosAdicionales
                    + " más";
        }

        return productoPrincipal;
    }

    public boolean isCancelada() {
        return estado == EstadoOrden.CANCELADA;
    }

    public boolean isPasoConfirmada() {
        return estado != null
                && estado != EstadoOrden.CANCELADA;
    }

    public boolean isPasoProcesando() {
        return estado == EstadoOrden.PROCESANDO
                || estado == EstadoOrden.ENVIADO
                || estado == EstadoOrden.ENTREGADO;
    }

    public boolean isPasoEnviado() {
        return estado == EstadoOrden.ENVIADO
                || estado == EstadoOrden.ENTREGADO;
    }

    public boolean isPasoEntregado() {
        return estado == EstadoOrden.ENTREGADO;
    }

}
