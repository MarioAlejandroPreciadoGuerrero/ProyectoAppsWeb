package com.example.Bemole_API.service.mappers;

import com.example.Bemole_API.dto.ordenes.response.*;
import com.example.Bemole_API.models.Producto;
import org.springframework.stereotype.Component;

import com.example.Bemole_API.models.DireccionOrden;
import com.example.Bemole_API.models.ItemOrden;
import com.example.Bemole_API.models.Orden;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrdenMapper {
    public OrdenCreadaResponseDTO toCreadaResponseDTO(Orden orden) {
        List<ItemOrdenResponseDTO> items = orden.getItems()
                        .stream()
                        .map(this::toItemDTO)
                        .toList();

        BigDecimal subtotal = items.stream()
                .map(ItemOrdenResponseDTO::getSubtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        ContactoOrdenResponseDTO contacto = new ContactoOrdenResponseDTO(
                        orden.getContactoNombre(),
                        orden.getContactoApellido(),
                        orden.getContactoEmail(),
                        orden.getContactoTelefono()
                );

        DireccionOrdenResponseDTO direccion = toDireccionDTO(
                        orden.getDireccionEnvio()
                );

        return new OrdenCreadaResponseDTO(
                orden.getId(),
                orden.getNumero(),
                orden.getFecha(),
                orden.getEstado(),
                contacto,
                direccion,
                orden.getMetodoEnvio(),
                subtotal,
                orden.getCostoEnvio(),
                orden.getTotal(),
                items
        );
    }

    private ItemOrdenResponseDTO toItemDTO(ItemOrden item) {
        BigDecimal subtotalBruto = item.getPrecioUnitario()
                        .multiply(
                                BigDecimal.valueOf(
                                        item.getCantidad()
                                )
                        );

        BigDecimal subtotal = subtotalBruto.subtract(
                        item.getDescuento()
                );

        return new ItemOrdenResponseDTO(
                item.getId(),
                item.getProducto().getId(),
                item.getProducto().getNombre(),
                item.getCantidad(),
                item.getPrecioUnitario(),
                item.getDescuento(),
                subtotal
        );
    }

    private DireccionOrdenResponseDTO toDireccionDTO(DireccionOrden direccion) {
        if (direccion == null) {
            return null;
        }

        return new DireccionOrdenResponseDTO(
                direccion.getCalleNumero(),
                direccion.getColonia(),
                direccion.getCodigoPostal(),
                direccion.getCiudad(),
                direccion.getEstado()
        );
    }

    public OrdenResumenResponseDTO toResumenResponseDTO(
            Orden orden
    ) {
        int cantidadProductos = orden.getItems()
                .stream()
                .mapToInt(ItemOrden::getCantidad)
                .sum();

        String productoPrincipal = null;
        int productosAdicionales = 0;

        if (!orden.getItems().isEmpty()) {
            productoPrincipal = orden
                    .getItems()
                    .get(0)
                    .getProducto()
                    .getNombre();

            productosAdicionales =
                    orden.getItems().size() - 1;
        }

        return new OrdenResumenResponseDTO(
                orden.getId(),
                orden.getNumero(),
                orden.getFecha(),
                orden.getEstado(),
                orden.getTotal(),
                cantidadProductos,
                productoPrincipal,
                productosAdicionales
        );
    }

    public OrdenDetalleResponseDTO toDetalleResponseDTO(
            Orden orden
    ) {
        if (orden == null) {
            return null;
        }

        OrdenDetalleResponseDTO dto =
                new OrdenDetalleResponseDTO();

        dto.setId(orden.getId());
        dto.setNumero(orden.getNumero());
        dto.setFecha(orden.getFecha());
        dto.setEstado(orden.getEstado());
        dto.setEstadoPago(orden.getEstadoPago());
        dto.setMetodoEnvio(orden.getMetodoEnvio());

        dto.setContactoNombre(orden.getContactoNombre());
        dto.setContactoApellido(orden.getContactoApellido());
        dto.setContactoEmail(orden.getContactoEmail());
        dto.setContactoTelefono(orden.getContactoTelefono());

        if (orden.getUsuario() != null) {
            dto.setUsuarioEmail(
                    orden.getUsuario().getEmail()
            );
        }

        dto.setCostoEnvio(
                orden.getCostoEnvio() != null
                        ? orden.getCostoEnvio()
                        : BigDecimal.ZERO
        );

        dto.setDescuento(
                BigDecimal.ZERO
        );

        dto.setSubtotal(
                calcularSubtotal(orden)
        );

        dto.setTotal(
                orden.getTotal() != null
                        ? orden.getTotal()
                        : BigDecimal.ZERO
        );

        dto.setDireccionEnvio(
                convertirDireccion(orden.getDireccionEnvio())
        );

        dto.setItems(
                orden.getItems() == null
                        ? List.of()
                        : orden.getItems()
                        .stream()
                        .map(this::convertirItemDetalle)
                        .toList()
        );

        if (orden.getPago() != null) {
            dto.setMetodoPago(
                    orden.getPago().getMetodoPago()
            );

            dto.setPreferenceId(
                    orden.getPago().getPreferenceId()
            );

            dto.setMercadoPagoPaymentId(
                    orden.getPago().getMercadoPagoPaymentId()
            );
        }

        return dto;
    }

    private ItemOrdenDetalleResponseDTO convertirItemDetalle(ItemOrden item) {
        ItemOrdenDetalleResponseDTO dto =
                new ItemOrdenDetalleResponseDTO();

        Producto producto =
                item.getProducto();

        if (producto != null) {
            dto.setProductoId(
                    producto.getId()
            );

            dto.setNombreProducto(
                    producto.getNombre()
            );

            dto.setImagenUrl(
                    producto.getImagenUrl()
            );

            if (producto.getCategoria() != null) {
                dto.setCategoria(
                        producto.getCategoria().getNombre()
                );
            }
        }

        dto.setCantidad(
                item.getCantidad()
        );

        dto.setPrecioUnitario(
                item.getPrecioUnitario()
        );

        dto.setDescuento(
                item.getDescuento() != null
                        ? item.getDescuento()
                        : BigDecimal.ZERO
        );

        BigDecimal precio =
                item.getPrecioUnitario() != null
                        ? item.getPrecioUnitario()
                        : BigDecimal.ZERO;

        int cantidad =
                item.getCantidad() != null
                        ? item.getCantidad()
                        : 0;

        BigDecimal descuento =
                item.getDescuento() != null
                        ? item.getDescuento()
                        : BigDecimal.ZERO;

        dto.setSubtotal(
                precio.multiply(
                        BigDecimal.valueOf(cantidad)
                ).subtract(descuento)
        );

        return dto;
    }

    private DireccionOrdenResponseDTO convertirDireccion(DireccionOrden direccion) {
        if (direccion == null) {
            return null;
        }

        DireccionOrdenResponseDTO dto =
                new DireccionOrdenResponseDTO();

        dto.setCalleNumero(
                direccion.getCalleNumero()
        );

        dto.setColonia(
                direccion.getColonia()
        );

        dto.setCodigoPostal(
                direccion.getCodigoPostal()
        );

        dto.setCiudad(
                direccion.getCiudad()
        );

        dto.setEstado(
                direccion.getEstado()
        );

        return dto;
    }

    private BigDecimal calcularSubtotal(Orden orden) {
        if (orden.getItems() == null
                || orden.getItems().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return orden.getItems()
                .stream()
                .map(item -> {
                    BigDecimal precio =
                            item.getPrecioUnitario() != null
                                    ? item.getPrecioUnitario()
                                    : BigDecimal.ZERO;

                    int cantidad =
                            item.getCantidad() != null
                                    ? item.getCantidad()
                                    : 0;

                    BigDecimal descuento =
                            item.getDescuento() != null
                                    ? item.getDescuento()
                                    : BigDecimal.ZERO;

                    return precio.multiply(
                            BigDecimal.valueOf(cantidad)
                    ).subtract(descuento);
                })
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }
}
