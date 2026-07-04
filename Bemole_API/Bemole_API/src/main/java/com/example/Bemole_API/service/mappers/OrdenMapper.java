package com.example.Bemole_API.service.mappers;

import com.example.Bemole_API.dto.ordenes.response.*;
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
}
