package com.example.Bemole_API.contoller;

import com.example.Bemole_API.dto.PaginacionDTO;
import com.example.Bemole_API.dto.ordenes.request.CrearOrdenRequestDTO;
import com.example.Bemole_API.dto.ordenes.response.OrdenCreadaResponseDTO;
import com.example.Bemole_API.dto.ordenes.response.OrdenResumenResponseDTO;
import com.example.Bemole_API.enums.EstadoOrden;
import com.example.Bemole_API.models.Usuario;
import com.example.Bemole_API.service.OrdenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ordenes")
@AllArgsConstructor
public class OrdenController {
    @Autowired
    private final OrdenService service;

    @PostMapping
    public ResponseEntity<OrdenCreadaResponseDTO> crear(
            @AuthenticationPrincipal Usuario usuario,
            @Valid @RequestBody CrearOrdenRequestDTO request
    ) {
        OrdenCreadaResponseDTO orden =
                service.crearOrden(usuario, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(orden);
    }

    @GetMapping
    public ResponseEntity<PaginacionDTO<OrdenResumenResponseDTO>> listar(@AuthenticationPrincipal Usuario usuario, @RequestParam(required = false) EstadoOrden estado, @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "10") int tamano){
        return ResponseEntity.ok(service.listarOrdenes(usuario, estado, pagina, tamano));
    }

    @GetMapping("/pendiente")
    public ResponseEntity<OrdenCreadaResponseDTO> obtenerOrdenPendiente(
            @AuthenticationPrincipal Usuario usuario
    ) {
        OrdenCreadaResponseDTO response =
                service.obtenerOrdenPendiente(
                        usuario.getId()
                );

        return ResponseEntity.ok(response);
    }
}
