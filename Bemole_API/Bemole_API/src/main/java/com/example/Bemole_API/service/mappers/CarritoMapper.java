package com.example.Bemole_API.service.mappers;

import org.springframework.stereotype.Component;
import com.example.Bemole_API.dto.carrito.CarritoResponseDTO;
import com.example.Bemole_API.dto.carrito.ItemCarritoResponseDTO;
import com.example.Bemole_API.models.Carrito;
import com.example.Bemole_API.models.ItemCarrito;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CarritoMapper {
    public CarritoResponseDTO toResponseDTO(Carrito carrito) {
        List<ItemCarritoResponseDTO> itemsDTO =
                carrito.getItems()
                        .stream()
                        .map(this::toItemDTO)
                        .toList();

        int cantidadProductos =
                carrito.getItems()
                        .stream()
                        .mapToInt(ItemCarrito::getCantidad)
                        .sum();

        BigDecimal subtotal =
                itemsDTO.stream()
                        .map(ItemCarritoResponseDTO::getSubtotal)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal descuento = BigDecimal.ZERO;
        BigDecimal total = subtotal.subtract(descuento);

        return new CarritoResponseDTO(
                carrito.getId(),
                itemsDTO,
                itemsDTO.size(),
                cantidadProductos,
                subtotal,
                descuento,
                total
        );
    }

    private ItemCarritoResponseDTO toItemDTO(
            ItemCarrito item
    ) {
        BigDecimal precio =
                item.getProducto().getPrecio();

        BigDecimal subtotal =
                precio.multiply(
                        BigDecimal.valueOf(item.getCantidad())
                );

        return new ItemCarritoResponseDTO(
                item.getId(),
                item.getProducto().getId(),
                item.getProducto().getNombre(),
                item.getProducto()
                        .getCategoria()
                        .getNombre(),
                precio,
                item.getCantidad(),
                item.getProducto().getStock(),
                subtotal
        );
    }
}
