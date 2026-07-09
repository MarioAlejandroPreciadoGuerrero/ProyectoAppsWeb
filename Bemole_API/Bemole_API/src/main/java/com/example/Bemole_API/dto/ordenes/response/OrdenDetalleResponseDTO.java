package com.example.Bemole_API.dto.ordenes.response;

import com.example.Bemole_API.enums.EstadoOrden;
import com.example.Bemole_API.enums.EstadoPago;
import com.example.Bemole_API.enums.MetodoEnvio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenDetalleResponseDTO {
    private Long id;
    private String numero;
    private LocalDateTime fecha;

    private EstadoOrden estado;
    private EstadoPago estadoPago;
    private MetodoEnvio metodoEnvio;

    private String contactoNombre;
    private String contactoApellido;
    private String contactoEmail;
    private String contactoTelefono;

    private String usuarioEmail;

    private DireccionOrdenResponseDTO direccionEnvio;

    private List<ItemOrdenDetalleResponseDTO> items;

    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal costoEnvio;
    private BigDecimal total;

    private String metodoPago;
    private String preferenceId;
    private String mercadoPagoPaymentId;

    public String getFechaTexto() {
        if (fecha == null) {
            return "Fecha no disponible";
        }

        return fecha.format(
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm"
                )
        );
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
            case PENDIENTE -> "⌛";
            case PROCESANDO -> "📦";
            case ENVIADO -> "🚚";
            case ENTREGADO -> "✓";
            case CANCELADA -> "✕";
        };
    }

    public String getEstadoCss() {
        if (estado == null) {
            return "status-badge--pending";
        }

        return switch (estado) {
            case PENDIENTE -> "status-badge--pending";
            case PROCESANDO -> "status-badge--processing";
            case ENVIADO -> "status-badge--transit";
            case ENTREGADO -> "status-badge--delivered";
            case CANCELADA -> "status-badge--cancelled";
        };
    }

    public Integer getCantidadProductos() {
        if (items == null || items.isEmpty()) {
            return 0;
        }

        return items.stream()
                .map(ItemOrdenDetalleResponseDTO::getCantidad)
                .filter(cantidad -> cantidad != null)
                .mapToInt(Integer::intValue)
                .sum();
    }
}
