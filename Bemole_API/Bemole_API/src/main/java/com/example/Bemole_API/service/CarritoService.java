package com.example.Bemole_API.service;

import com.example.Bemole_API.dto.carrito.ActualizarCantidadItemRequestDTO;
import com.example.Bemole_API.dto.carrito.AgregarItemCarritoRequestDTO;
import com.example.Bemole_API.dto.carrito.CarritoResponseDTO;
import com.example.Bemole_API.exception.RecursoNoEncontradoException;
import com.example.Bemole_API.exception.NegocioException;
import com.example.Bemole_API.service.mappers.CarritoMapper;
import com.example.Bemole_API.models.Carrito;
import com.example.Bemole_API.models.ItemCarrito;
import com.example.Bemole_API.models.Producto;
import com.example.Bemole_API.models.Usuario;
import com.example.Bemole_API.repositorys.CarritoRepository;
import com.example.Bemole_API.repositorys.ItemCarritoRepository;
import com.example.Bemole_API.repositorys.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class CarritoService {
    @Autowired
    private final CarritoRepository carritoRepository;
    @Autowired
    private final ItemCarritoRepository itemCarritoRepository;
    @Autowired
    private final ProductoRepository productoRepository;
    @Autowired
    private final CarritoMapper carritoMapper;

    @Transactional
    public CarritoResponseDTO obtenerCarrito(Usuario usuario) {
        validarUsuarioAutenticado(usuario);

        Carrito carrito = obtenerOCrearCarrito(usuario);

        Carrito carritoDetalle = carritoRepository.findDetalleByUsuarioId(usuario.getId()).orElse(carrito);

        return carritoMapper.toResponseDTO(carritoDetalle);
    }

    @Transactional
    public CarritoResponseDTO agregarItem(Usuario usuario, AgregarItemCarritoRequestDTO request) {
        validarUsuarioAutenticado(usuario);

        Producto producto = productoRepository.findById(request.getProductoId()).orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró el producto solicitado."
                        )
                );

        validarProductoDisponible(
                producto,
                request.getCantidad()
        );

        Carrito carrito = obtenerOCrearCarrito(usuario);

        ItemCarrito itemExistente = itemCarritoRepository.findByCarritoIdAndProductoId(carrito.getId(), producto.getId()).orElse(null);

        if (itemExistente != null) {
            int nuevaCantidad = itemExistente.getCantidad() + request.getCantidad();

            validarStock(producto, nuevaCantidad);

            itemExistente.setCantidad(nuevaCantidad);
            itemCarritoRepository.save(itemExistente);

        } else {
            ItemCarrito nuevoItem = new ItemCarrito();

            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(request.getCantidad());

            itemCarritoRepository.save(nuevoItem);
        }

        return obtenerCarritoActualizado(usuario.getId());
    }

    @Transactional
    public CarritoResponseDTO actualizarCantidad(Usuario usuario, Long itemId, ActualizarCantidadItemRequestDTO request) {
        validarUsuarioAutenticado(usuario);
        validarItemId(itemId);

        ItemCarrito item = itemCarritoRepository.findByIdAndCarritoUsuarioId(itemId, usuario.getId()).orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró el artículo en tu carrito."
                        )
                );

        Producto producto = item.getProducto();

        validarProductoDisponible(producto, request.getCantidad());

        item.setCantidad(request.getCantidad());
        itemCarritoRepository.save(item);

        return obtenerCarritoActualizado(usuario.getId());
    }

    @Transactional
    public CarritoResponseDTO eliminarItem(Usuario usuario, Long itemId) {
        validarUsuarioAutenticado(usuario);
        validarItemId(itemId);

        ItemCarrito item = itemCarritoRepository.findByIdAndCarritoUsuarioId(itemId, usuario.getId()).orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró el artículo en tu carrito."
                        )
                );

        itemCarritoRepository.delete(item);

        return obtenerCarritoActualizado(usuario.getId());
    }

    private Carrito obtenerOCrearCarrito(Usuario usuario) {
        return carritoRepository.findByUsuarioId(usuario.getId()).orElseGet(() -> {
            Carrito carrito = new Carrito();

            carrito.setUsuario(usuario);
            carrito.setFechaCreacion(
                    LocalDateTime.now()
            );
            carrito.setItems(new ArrayList<>());

            return carritoRepository.save(carrito);
        });
    }

    private CarritoResponseDTO obtenerCarritoActualizado(Long usuarioId) {
        Carrito carrito = carritoRepository.findDetalleByUsuarioId(usuarioId).orElseThrow(() -> new RecursoNoEncontradoException(
                "No se encontró el carrito del usuario."
                )
        );

        return carritoMapper.toResponseDTO(carrito);
    }

    private void validarProductoDisponible(Producto producto, int cantidad) {
        if (!producto.getActivo()) {
            throw new NegocioException(
                    "El producto no está disponible para la venta."
            );
        }

        validarStock(producto, cantidad);
    }

    private void validarStock(Producto producto, int cantidad) {
        if (producto.getStock() <= 0) {
            throw new NegocioException(
                    "El producto no tiene existencias disponibles."
            );
        }

        if (cantidad > producto.getStock()) {
            throw new NegocioException(
                    "La cantidad solicitada supera el stock disponible. " + "Existencias actuales: " + producto.getStock() + "."
            );
        }
    }

    private void validarUsuarioAutenticado(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            throw new NegocioException(
                    "No fue posible identificar al usuario autenticado."
            );
        }
    }

    private void validarItemId(Long itemId) {
        if (itemId == null || itemId <= 0) {
            throw new IllegalArgumentException(
                    "El ID del artículo debe ser positivo."
            );
        }
    }
}
