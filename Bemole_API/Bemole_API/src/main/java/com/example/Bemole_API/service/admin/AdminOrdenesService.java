package com.example.Bemole_API.service.admin;

import com.example.Bemole_API.dto.PaginacionDTO;
import com.example.Bemole_API.dto.ordenes.response.OrdenResumenResponseDTO;
import com.example.Bemole_API.enums.EstadoOrden;
import com.example.Bemole_API.exception.NegocioException;
import com.example.Bemole_API.exception.RecursoNoEncontradoException;
import com.example.Bemole_API.models.Orden;
import com.example.Bemole_API.models.Usuario;
import com.example.Bemole_API.repositorys.OrdenRepository;
import com.example.Bemole_API.service.mappers.OrdenMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminOrdenesService {
    private final OrdenRepository repository;
    private final OrdenMapper ordenMapper;

    @Transactional
    public Page<OrdenResumenResponseDTO> listarOrdenes(EstadoOrden estado, Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("La configuración de paginación es obligatoria.");
        }

        Page<Orden> paginaOrdenes;

        if (estado == null) {
            paginaOrdenes =
                    repository.findAll(pageable);
        } else {
            paginaOrdenes = repository.findByEstado(estado,pageable);
        }

        return paginaOrdenes.map(
                ordenMapper::toResumenResponseDTO
        );
    }

    @Transactional
    public OrdenResumenResponseDTO actualizarEstado(Long ordenId, EstadoOrden nuevoEstado) {
        Orden orden = repository.findById(ordenId).orElseThrow(() ->
                new RecursoNoEncontradoException("Orden con ID " + ordenId + " no encontrada.")
        );

        validarTransicion(
                orden.getEstado(),
                nuevoEstado
        );

        orden.setEstado(nuevoEstado);

        return ordenMapper.toResumenResponseDTO(repository.save(orden));
    }

    private void validarTransicion(EstadoOrden actual, EstadoOrden nuevo) {
        if (actual == nuevo) {
            return;
        }

        boolean permitida = switch (actual) {

            case PENDIENTE ->
                    nuevo == EstadoOrden.PROCESANDO || nuevo == EstadoOrden.CANCELADA;

            case PROCESANDO ->
                    nuevo == EstadoOrden.ENVIADO || nuevo == EstadoOrden.CANCELADA;

            case ENVIADO ->
                    nuevo == EstadoOrden.ENTREGADO;

            case ENTREGADO, CANCELADA -> false;
        };

        if (!permitida) {
            throw new NegocioException("No se puede cambiar una orden de " + actual + " a " + nuevo + ".");
        }
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


}
