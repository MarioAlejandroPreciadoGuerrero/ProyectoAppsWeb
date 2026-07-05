package com.example.Bemole_API.service.admin;

import com.example.Bemole_API.dto.admin.AdminResumenDTO;
import com.example.Bemole_API.enums.EstadoOrden;
import com.example.Bemole_API.repositorys.CategoriaRepository;
import com.example.Bemole_API.repositorys.OrdenRepository;
import com.example.Bemole_API.repositorys.ProductoRepository;
import com.example.Bemole_API.repositorys.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class AdminResumenService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final OrdenRepository ordenRepository;

    public AdminResumenDTO obtenerResumen() {
        BigDecimal ventas = ordenRepository.sumarVentasExceptoEstado(EstadoOrden.CANCELADA);

        return new AdminResumenDTO(
                productoRepository.count(),
                categoriaRepository.count(),
                usuarioRepository.count(),
                ordenRepository.count(),
                ordenRepository.countByEstado(EstadoOrden.PENDIENTE),
                ventas != null ? ventas : BigDecimal.ZERO
        );
    }
}
