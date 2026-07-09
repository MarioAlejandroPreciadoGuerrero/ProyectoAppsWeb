package com.example.Bemole_API.service;

import com.example.Bemole_API.dto.PaginacionDTO;
import com.example.Bemole_API.dto.ordenes.request.CrearOrdenRequestDTO;
import com.example.Bemole_API.dto.ordenes.response.OrdenCreadaResponseDTO;
import com.example.Bemole_API.dto.ordenes.response.OrdenResumenResponseDTO;
import com.example.Bemole_API.enums.EstadoPago;
import com.example.Bemole_API.exception.RecursoNoEncontradoException;
import com.example.Bemole_API.exception.NegocioException;
import com.example.Bemole_API.repositorys.*;
import com.example.Bemole_API.service.mappers.OrdenMapper;
import com.example.Bemole_API.models.*;
import com.example.Bemole_API.enums.EstadoOrden;
import com.example.Bemole_API.enums.MetodoEnvio;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrdenService {
    private static final BigDecimal COSTO_ENVIO_ESTANDAR = BigDecimal.ZERO;

    private static final BigDecimal COSTO_ENVIO_EXPRESS = new BigDecimal("199.00");

    private static final BigDecimal COSTO_RECOGER_TIENDA = BigDecimal.ZERO;

    @Autowired
    private final OrdenRepository ordenRepository;
    @Autowired
    private final CarritoRepository carritoRepository;
    @Autowired
    private final ItemCarritoRepository itemCarritoRepository;
    @Autowired
    private final ProductoRepository productoRepository;

    @Autowired
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private final OrdenMapper ordenMapper;

    @Transactional
    public OrdenCreadaResponseDTO crearOrden(Usuario usuario, CrearOrdenRequestDTO request) {
        validarUsuarioAutenticado(usuario);

        Carrito carrito = carritoRepository.findDetalleByUsuarioId(usuario.getId())
                .orElseThrow(() ->
                        new NegocioException(
                                "No existe un carrito para el usuario."
                        )
                );

        validarCarrito(carrito);

        Orden orden = crearOrdenBase(usuario, request);

        orden.setEstadoPago(EstadoPago.PENDIENTE);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (ItemCarrito itemCarrito : carrito.getItems()) {

            Producto producto = productoRepository.findByIdForUpdate(
                            itemCarrito.getProducto().getId()
                    )
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException(
                                    "Uno de los productos del carrito ya no existe."
                            )
                    );

            validarProducto(
                    producto,
                    itemCarrito.getCantidad()
            );

            ItemOrden itemOrden = new ItemOrden();

            itemOrden.setProducto(producto);
            itemOrden.setCantidad(itemCarrito.getCantidad());
            itemOrden.setPrecioUnitario(producto.getPrecio());
            itemOrden.setDescuento(BigDecimal.ZERO);

            orden.agregarItem(itemOrden);

            BigDecimal subtotalItem =
                    producto.getPrecio().multiply(
                            BigDecimal.valueOf(
                                    itemCarrito.getCantidad()
                            )
                    );

            subtotal = subtotal.add(subtotalItem);

            descontarStock(
                    producto,
                    itemCarrito.getCantidad()
            );
        }

        BigDecimal costoEnvio =
                calcularCostoEnvio(
                        request.getMetodoEnvio()
                );

        BigDecimal total =
                subtotal.add(costoEnvio);

        orden.setCostoEnvio(costoEnvio);
        orden.setTotal(total);

        DireccionOrden direccion =
                crearDireccion(
                        request,
                        orden
                );

        orden.asignarDireccion(direccion);

        Orden ordenGuardada =
                ordenRepository.save(orden);

        vaciarCarrito(carrito);

        return ordenMapper.toCreadaResponseDTO(
                ordenGuardada
        );
    }

    public OrdenCreadaResponseDTO obtenerOrdenPendiente(
            Long usuarioId
    ) {
        Orden orden = ordenRepository
                .findFirstByUsuario_IdAndEstadoPagoOrderByFechaDesc(
                        usuarioId,
                        EstadoPago.PENDIENTE
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "No tienes órdenes pendientes de pago"
                        )
                );

        return ordenMapper.toCreadaResponseDTO(orden);
    }

    public PaginacionDTO<OrdenResumenResponseDTO> listarOrdenes(Usuario usuario, EstadoOrden estado, int pagina, int tamano) {
        validarUsuarioAutenticado(usuario);
        validarPaginacion(pagina, tamano);

        Pageable pageable = PageRequest.of(
                pagina,
                tamano,
                Sort.by(
                        Sort.Direction.DESC,
                        "fecha"
                )
        );

        Page<Orden> resultado;

        if (estado == null) {
            resultado = ordenRepository.findByUsuarioId(
                    usuario.getId(),
                    pageable
            );
        } else {
            resultado =
                    ordenRepository
                            .findByUsuarioIdAndEstado(
                                    usuario.getId(),
                                    estado,
                                    pageable
                            );
        }

        List<OrdenResumenResponseDTO> contenido =
                resultado.getContent()
                        .stream()
                        .map(
                                ordenMapper::toResumenResponseDTO
                        )
                        .toList();

        return new PaginacionDTO<>(contenido, resultado.getNumber(), resultado.getSize(), resultado.getTotalElements(), resultado.getTotalPages(), resultado.isFirst(), resultado.isLast());
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

    private Orden crearOrdenBase(Usuario usuario, CrearOrdenRequestDTO request) {
        Orden orden = new Orden();

        orden.setUsuario(usuario);
        orden.setNumero(generarNumeroOrden());
        orden.setFecha(LocalDateTime.now());
        orden.setEstado(EstadoOrden.PENDIENTE);

        orden.setContactoNombre(request.getContacto().getNombre().trim());

        orden.setContactoApellido(request.getContacto().getApellido().trim());

        orden.setContactoEmail(request.getContacto().getEmail().trim().toLowerCase());

        orden.setContactoTelefono(request.getContacto().getTelefono().trim());

        orden.setMetodoEnvio(request.getMetodoEnvio());

        orden.setCostoEnvio(BigDecimal.ZERO);
        orden.setTotal(BigDecimal.ZERO);

        return orden;
    }

    private ItemOrden convertirAItemOrden(ItemCarrito itemCarrito) {
        ItemOrden itemOrden = new ItemOrden();

        itemOrden.setProducto(itemCarrito.getProducto());

        itemOrden.setCantidad(itemCarrito.getCantidad());

        itemOrden.setPrecioUnitario(itemCarrito.getProducto().getPrecio());

        itemOrden.setDescuento(BigDecimal.ZERO);

        return itemOrden;
    }

    private DireccionOrden crearDireccion(CrearOrdenRequestDTO request, Orden orden) {
        DireccionOrden direccion = new DireccionOrden();

        direccion.setOrden(orden);

        direccion.setCalleNumero(request.getDireccionEnvio().getCalleNumero().trim());

        direccion.setColonia(request.getDireccionEnvio().getColonia().trim());

        direccion.setCodigoPostal(request.getDireccionEnvio().getCodigoPostal().trim());

        direccion.setCiudad(request.getDireccionEnvio().getCiudad().trim());

        direccion.setEstado(request.getDireccionEnvio().getEstado().trim());

        return direccion;
    }

    private void descontarStock(Producto producto, int cantidad) {
        int nuevoStock = producto.getStock() - cantidad;

        producto.setStock(nuevoStock);

        productoRepository.save(producto);
    }

    private void vaciarCarrito(Carrito carrito) {
        List<ItemCarrito> items = List.copyOf(carrito.getItems());

        itemCarritoRepository.deleteAll(items);

        carrito.getItems().clear();
    }

    private BigDecimal calcularCostoEnvio(MetodoEnvio metodoEnvio) {
        return switch (metodoEnvio) {
            case ESTANDAR -> COSTO_ENVIO_ESTANDAR;

            case EXPRESS -> COSTO_ENVIO_EXPRESS;

            case RECOGER_TIENDA -> COSTO_RECOGER_TIENDA;
        };
    }

    private String generarNumeroOrden() {
        String numero;

        do {
            String fragmento = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            numero = "BMO-" + Year.now().getValue() + "-" + fragmento;

        } while (ordenRepository.existsByNumero(numero));

        return numero;
    }

    private void validarCarrito(Carrito carrito) {
        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new NegocioException(
                    "No puedes crear una orden con el carrito vacío."
            );
        }
    }

    private void validarProducto(Producto producto, int cantidad) {
        if (producto == null) {
            throw new RecursoNoEncontradoException(
                    "Uno de los productos del carrito ya no existe."
            );
        }

        if (!producto.getActivo()) {
            throw new NegocioException(
                    "El producto '" + producto.getNombre() + "' ya no está disponible."
            );
        }

        if (cantidad <= 0) {
            throw new NegocioException(
                    "La cantidad de un producto del carrito no es válida."
            );
        }

        if (producto.getStock() < cantidad) {
            throw new NegocioException(
                    "No hay stock suficiente para el producto '" + producto.getNombre() + "'. Existencias actuales: " + producto.getStock() + "."
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
}
