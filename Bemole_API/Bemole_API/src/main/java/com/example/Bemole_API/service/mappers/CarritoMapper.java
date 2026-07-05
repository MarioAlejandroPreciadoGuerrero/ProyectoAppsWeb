package com.example.Bemole_API.service.mappers;

import com.example.Bemole_API.service.ImagenUrlService;
import org.springframework.stereotype.Component;
import com.example.Bemole_API.dto.carrito.CarritoResponseDTO;
import com.example.Bemole_API.dto.carrito.ItemCarritoResponseDTO;
import com.example.Bemole_API.models.Carrito;
import com.example.Bemole_API.models.ItemCarrito;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CarritoMapper {
    private final ImagenUrlService imagenUrlService;

    public CarritoMapper( ImagenUrlService imagenUrlService){
        this.imagenUrlService = imagenUrlService;
    }

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

    private ItemCarritoResponseDTO toItemDTO(ItemCarrito item) {
        BigDecimal precio = item.getProducto().getPrecio();

        BigDecimal subtotal = precio.multiply(
                BigDecimal.valueOf(item.getCantidad()));

        ItemCarritoResponseDTO dto = new ItemCarritoResponseDTO();

        dto.setId(item.getId());
        dto.setProductoId(item.getProducto().getId());
        dto.setCategoria(item.getProducto().getCategoria().getNombre());
        dto.setNombreProducto(item.getProducto().getNombre());
        dto.setStockDisponible(item.getProducto().getStock());
        dto.setPrecioUnitario(precio);
        dto.setSubtotal(subtotal);
        dto.setCantidad(item.getCantidad());
        dto.setImagenUrl(imagenUrlService.construirUrl(item.getProducto().getImagenUrl()));

        return dto;
    }
}
