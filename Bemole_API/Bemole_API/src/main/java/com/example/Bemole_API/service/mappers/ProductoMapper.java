package com.example.Bemole_API.service.mappers;

import com.example.Bemole_API.dto.categoria.CategoriaResumenDTO;
import com.example.Bemole_API.dto.producto.CategoriaProductoDTO;
import com.example.Bemole_API.dto.producto.ProductoDetalleDTO;
import com.example.Bemole_API.dto.producto.ProductoResumenDTO;
import com.example.Bemole_API.models.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {




    public ProductoResumenDTO toResumenDTO(Producto producto) {
        if (producto == null) {
            return null;
        }

        CategoriaProductoDTO categoriaDTO =
                new CategoriaProductoDTO(
                        producto.getCategoria().getId(),
                        producto.getCategoria().getNombre()
                );

        return new ProductoResumenDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getActivo() && producto.getStock() > 0,
                categoriaDTO,
                producto.getImagenUrl()
        );
    }

    public ProductoDetalleDTO toDetalleDTO(Producto producto) {
        if (producto == null) {
            return null;
        }

        CategoriaProductoDTO categoriaDTO =
                new CategoriaProductoDTO(
                        producto.getCategoria().getId(),
                        producto.getCategoria().getNombre()
                );

        return new ProductoDetalleDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getActivo(),
                producto.getActivo() && producto.getStock() > 0,
                categoriaDTO,
                producto.getImagenUrl()
        );
    }
}
