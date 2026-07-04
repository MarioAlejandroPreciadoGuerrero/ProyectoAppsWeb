package com.example.Bemole_API.service;

import com.example.Bemole_API.dto.PaginacionDTO;
import com.example.Bemole_API.dto.producto.enums.OrdenProducto;
import com.example.Bemole_API.dto.producto.ProductoDetalleDTO;
import com.example.Bemole_API.dto.producto.ProductoResumenDTO;
import com.example.Bemole_API.exception.NegocioException;
import com.example.Bemole_API.exception.RecursoNoEncontradoException;
import com.example.Bemole_API.service.mappers.ProductoMapper;
import com.example.Bemole_API.models.Producto;
import com.example.Bemole_API.repositorys.ProductoRepository;
import com.example.Bemole_API.repositorys.specifications.ProductoSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductoService {

    @Autowired
    private ProductoRepository repository;

    @Autowired
    private final ProductoMapper productoMapper;

    public PaginacionDTO<ProductoResumenDTO> listarCatalogo(String q, Long categoriaId, BigDecimal precioMin, BigDecimal precioMax, Boolean soloConStock, OrdenProducto orden, int pagina, int tamano) {
        validarPaginacion(pagina, tamano);
        validarRangoPrecios(precioMin, precioMax);

        Specification<Producto> specification =
                Specification.where(
                        ProductoSpecifications.activo()).and(ProductoSpecifications.nombreContiene(q)).and(
                        ProductoSpecifications.categoriaIgual(categoriaId)).and(
                        ProductoSpecifications.precioMayorOIgual(precioMin)).and(
                        ProductoSpecifications.precioMenorOIgual(precioMax)).and(ProductoSpecifications.conStock(soloConStock));

        Pageable pageable = PageRequest.of(
                pagina,
                tamano,
                crearOrden(orden)
        );

        Page<Producto> resultado =
                repository.findAll(
                        specification,
                        pageable
                );

        List<ProductoResumenDTO> contenido =
                resultado.getContent()
                        .stream()
                        .map(productoMapper::toResumenDTO)
                        .toList();

        return new PaginacionDTO<>(contenido, resultado.getNumber(), resultado.getSize(), resultado.getTotalElements(), resultado.getTotalPages(), resultado.isFirst(), resultado.isLast());
    }

    public ProductoDetalleDTO obtenerDetalle(Long productoId) {
        if (productoId == null || productoId <= 0) {
            throw new IllegalArgumentException(
                    "El ID del producto debe ser positivo."
            );
        }

        Producto producto = repository
                .findById(productoId)
                .filter(Producto::getActivo)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró el producto solicitado."
                        )
                );

        return productoMapper.toDetalleDTO(producto);
    }

    public Producto crearProducto(Producto producto) {
        validarProductoCompleto(producto);

        String nombreNormalizado = producto.getNombre().trim();
        if (repository.existsByNombreIgnoreCase(nombreNormalizado)) {
            throw new NegocioException("Ya existe un producto con el nombre: " + nombreNormalizado);
        }

        producto.setNombre(nombreNormalizado);
        if (producto.getDescripcion() != null) {
            producto.setDescripcion(producto.getDescripcion().trim());
        }

        return repository.save(producto);
    }

    public Producto actualizarProductoCompleto(Long id, Producto productoActualizado) {
        if (id == null || id <= 0) {
            throw new NegocioException("El ID del producto debe ser un número positivo.");
        }
        validarProductoCompleto(productoActualizado);

        return repository.findById(id).map(producto -> {
            producto.setNombre(productoActualizado.getNombre().trim());
            producto.setActivo(productoActualizado.getActivo());
            producto.setCategoria(productoActualizado.getCategoria());
            producto.setDescripcion(productoActualizado.getDescripcion() != null
                    ? productoActualizado.getDescripcion().trim() : null);
            producto.setPrecio(productoActualizado.getPrecio());
            producto.setStock(productoActualizado.getStock());
            return repository.save(producto);
        }).orElseThrow(() -> new RecursoNoEncontradoException("Producto con ID " + id + " no encontrado."));
    }

    public Producto actualizarProductoParcial(Long id, Producto productoParcial) {
        if (id == null || id <= 0) {
            throw new NegocioException("El ID del producto debe ser un número positivo.");
        }
        if (productoParcial == null) {
            throw new NegocioException("Los datos del producto no pueden ser nulos.");
        }

        return repository.findById(id).map(producto -> {

            if (productoParcial.getNombre() != null) {
                String nuevoNombre = productoParcial.getNombre().trim();
                if (nuevoNombre.isEmpty()) {
                    throw new NegocioException("El nombre del producto no puede estar vacío.");
                }
                if (nuevoNombre.length() > 150) {
                    throw new NegocioException("El nombre del producto no puede superar los 150 caracteres.");
                }
                if (!nuevoNombre.equalsIgnoreCase(producto.getNombre())
                        && repository.existsByNombreIgnoreCase(nuevoNombre)) {
                    throw new NegocioException("Ya existe un producto con el nombre: " + nuevoNombre);
                }
                producto.setNombre(nuevoNombre);
            }

            if (productoParcial.getCategoria() != null) {
                producto.setCategoria(productoParcial.getCategoria());
            }

            if (productoParcial.getActivo() != null) {
                producto.setActivo(productoParcial.getActivo());
            }

            if (productoParcial.getPrecio() != null) {
                validarPrecio(productoParcial.getPrecio());
                producto.setPrecio(productoParcial.getPrecio());
            }

            if (productoParcial.getStock() != null) {
                validarStock(productoParcial.getStock());
                producto.setStock(productoParcial.getStock());
            }

            if (productoParcial.getDescripcion() != null) {
                String nuevaDesc = productoParcial.getDescripcion().trim();
                if (nuevaDesc.length() > 1000) {
                    throw new NegocioException("La descripción no puede superar los 1000 caracteres.");
                }
                producto.setDescripcion(nuevaDesc);
            }

            return repository.save(producto);

        }).orElseThrow(() -> new RecursoNoEncontradoException("Producto con ID " + id + " no encontrado."));
    }

    public void eliminarProducto(Long id) {
        if (id == null || id <= 0) {
            throw new NegocioException("El ID del producto debe ser un número positivo.");
        }
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Producto con ID " + id + " no encontrado.");
        }
        repository.deleteById(id);
    }

    // Metodos Auxiliares

    private void validarProductoCompleto(Producto producto) {
        if (producto == null) {
            throw new NegocioException("El producto no puede ser nulo.");
        }
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new NegocioException("El nombre del producto es obligatorio.");
        }
        if (producto.getNombre().trim().length() > 150) {
            throw new NegocioException("El nombre del producto no puede superar los 150 caracteres.");
        }
        if (producto.getPrecio() == null) {
            throw new NegocioException("El precio del producto es obligatorio.");
        }
        validarPrecio(producto.getPrecio());

        if (producto.getStock() == null) {
            throw new NegocioException("El stock del producto es obligatorio.");
        }
        validarStock(producto.getStock());

        if (producto.getCategoria() == null) {
            throw new NegocioException("La categoría del producto es obligatoria.");
        }
        if (producto.getActivo() == null) {
            throw new NegocioException("El campo 'activo' del producto es obligatorio.");
        }
        if (producto.getDescripcion() != null && producto.getDescripcion().trim().length() > 1000) {
            throw new NegocioException("La descripción no puede superar los 1000 caracteres.");
        }
    }

    private void validarPrecio(BigDecimal precio) {
        if (precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegocioException("El precio no puede ser negativo.");
        }
        if (precio.compareTo(new BigDecimal("999999.99")) > 0) {
            throw new NegocioException("El precio excede el valor máximo permitido.");
        }
    }

    private void validarStock(Integer stock) {
        if (stock < 0) {
            throw new NegocioException("El stock no puede ser negativo.");
        }
    }

    private Sort crearOrden(OrdenProducto orden) {
        if (orden == null) {
            return Sort.by(
                    Sort.Direction.ASC,
                    "nombre"
            );
        }

        return switch (orden) {
            case NOMBRE_ASC ->
                    Sort.by(Sort.Direction.ASC, "nombre");

            case NOMBRE_DESC ->
                    Sort.by(Sort.Direction.DESC, "nombre");

            case PRECIO_ASC ->
                    Sort.by(Sort.Direction.ASC, "precio");

            case PRECIO_DESC ->
                    Sort.by(Sort.Direction.DESC, "precio");
        };
    }

    private void validarPaginacion(int pagina, int tamano) {
        if (pagina < 0) {
            throw new IllegalArgumentException(
                    "La página no puede ser negativa."
            );
        }

        if (tamano < 1 || tamano > 50) {
            throw new IllegalArgumentException(
                    "El tamaño de página debe estar entre 1 y 50."
            );
        }
    }

    private void validarRangoPrecios(
            BigDecimal precioMin,
            BigDecimal precioMax
    ) {
        if (precioMin != null
                && precioMin.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "El precio mínimo no puede ser negativo."
            );
        }

        if (precioMax != null
                && precioMax.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "El precio máximo no puede ser negativo."
            );
        }

        if (precioMin != null
                && precioMax != null
                && precioMin.compareTo(precioMax) > 0) {
            throw new IllegalArgumentException(
                    "El precio mínimo no puede ser mayor que el máximo."
            );
        }
    }
}
