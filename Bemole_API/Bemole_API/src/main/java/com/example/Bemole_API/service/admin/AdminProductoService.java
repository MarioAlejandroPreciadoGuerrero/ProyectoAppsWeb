package com.example.Bemole_API.service.admin;

import com.example.Bemole_API.dto.admin.request.ProductoAdminRequestDTO;
import com.example.Bemole_API.dto.admin.response.ProductoAdminResponseDTO;
import com.example.Bemole_API.exception.RecursoNoEncontradoException;
import com.example.Bemole_API.exception.NegocioException;
import com.example.Bemole_API.models.Categoria;
import com.example.Bemole_API.models.Producto;
import com.example.Bemole_API.repositorys.CategoriaRepository;
import com.example.Bemole_API.repositorys.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public Page<ProductoAdminResponseDTO> listarProductos(Pageable pageable) {
        return productoRepository.findAll(pageable).map(this::convertir);
    }

    public ProductoAdminResponseDTO obtenerPorId(Long id) {
        return convertir(buscarProducto(id));
    }

    @Transactional
    public ProductoAdminResponseDTO crearProducto(ProductoAdminRequestDTO request) {
        String nombre = request.getNombre().trim();

        if (productoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new NegocioException("Ya existe un producto con ese nombre.");
        }

        Categoria categoria = buscarCategoria(request.getCategoriaId());

        Producto producto = new Producto();

        aplicarDatos(producto, request, categoria);

        return convertir(productoRepository.save(producto));
    }

    @Transactional
    public ProductoAdminResponseDTO actualizar(Long id, ProductoAdminRequestDTO request) {
        Producto producto = buscarProducto(id);

        String nombre = request.getNombre().trim();

        if (productoRepository
                .existsByNombreIgnoreCaseAndIdNot(
                        nombre,
                        id
                )) {
            throw new NegocioException(
                    "Ya existe otro producto con ese nombre."
            );
        }

        Categoria categoria = buscarCategoria(request.getCategoriaId());

        aplicarDatos(producto, request, categoria);

        return convertir(productoRepository.save(producto));
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = buscarProducto(id);

        /*
         * Si Producto tiene relaciones con ItemOrden o ItemCarrito,
         * primero debes comprobarlas y rechazar la eliminación.
         *
         * No uses cascade REMOVE hacia el historial de órdenes.
         */

        productoRepository.delete(producto);
    }

    @Transactional
    public ProductoAdminResponseDTO actualizarActivo(Long id, Boolean activo) {
        if (activo == null) {
            throw new IllegalArgumentException("El estado activo es obligatorio.");
        }

        Producto producto = buscarProducto(id);

        producto.setActivo(activo);

        return convertir(productoRepository.save(producto));
    }

    private void aplicarDatos(Producto producto, ProductoAdminRequestDTO request, Categoria categoria) {
        producto.setNombre(request.getNombre().trim());

        producto.setDescripcion(request.getDescripcion() != null ? request.getDescripcion().trim() : null);

        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setActivo(request.getActivo());
        producto.setCategoria(categoria);
    }

    private Producto buscarProducto(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "El ID del producto no es válido."
            );
        }

        return productoRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Producto con ID " + id + " no encontrado.")
        );
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Categoría con ID " + id + " no encontrada.")
        );
    }

    private ProductoAdminResponseDTO convertir(Producto producto) {
        Categoria categoria = producto.getCategoria();

        return new ProductoAdminResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getActivo(),
                categoria != null ? categoria.getId() : null,
                categoria != null ? categoria.getNombre() : null
        );
    }
}
