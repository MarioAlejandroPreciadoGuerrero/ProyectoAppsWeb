package com.example.Bemole_API.contoller.admin;

import com.example.Bemole_API.dto.admin.request.ActualizarEstadoOrdenDTO;
import com.example.Bemole_API.dto.ordenes.response.OrdenResumenResponseDTO;
import com.example.Bemole_API.enums.EstadoOrden;
import com.example.Bemole_API.service.admin.AdminOrdenesService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/ordenes")
@AllArgsConstructor
public class AdminOrdenController {
    private final AdminOrdenesService service;

    @GetMapping
    public Page<OrdenResumenResponseDTO> listar(
            @RequestParam(required = false)
            EstadoOrden estado,

            @PageableDefault(
                    size = 10,
                    sort = "fecha",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return service.listarOrdenes(estado, pageable);
    }

    @PatchMapping("/{id}/estado")
    public OrdenResumenResponseDTO actualizarEstado(@PathVariable Long id, @Valid @RequestBody ActualizarEstadoOrdenDTO request) {
        return service.actualizarEstado(id, request.getEstado());
    }
}
