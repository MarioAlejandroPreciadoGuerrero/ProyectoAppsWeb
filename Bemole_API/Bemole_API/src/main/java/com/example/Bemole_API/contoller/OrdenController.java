package com.example.Bemole_API.contoller;

import com.example.Bemole_API.dto.ordenes.request.CrearOrdenRequestDTO;
import com.example.Bemole_API.dto.ordenes.response.OrdenCreadaResponseDTO;
import com.example.Bemole_API.models.Usuario;
import com.example.Bemole_API.service.OrdenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ordenes")
@AllArgsConstructor
public class OrdenController {
    @Autowired
    private final OrdenService service;

    @PostMapping
    public ResponseEntity<OrdenCreadaResponseDTO> crear(@AuthenticationPrincipal Usuario usuario, @Valid @RequestBody CrearOrdenRequestDTO request) {
        OrdenCreadaResponseDTO orden = service.crearOrden(usuario, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orden);
    }
}
