package com.example.Bemole_API.contoller;

import com.example.Bemole_API.dto.carrito.ActualizarCantidadItemRequestDTO;
import com.example.Bemole_API.dto.carrito.AgregarItemCarritoRequestDTO;
import com.example.Bemole_API.dto.carrito.CarritoResponseDTO;
import com.example.Bemole_API.models.Usuario;
import com.example.Bemole_API.service.CarritoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/carrito")
@AllArgsConstructor
public class CarritoController {
    @Autowired
    private final CarritoService service;

    @GetMapping
    public ResponseEntity<CarritoResponseDTO> obtener(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.obtenerCarrito(usuario));
    }

    @PostMapping("/items")
    public ResponseEntity<CarritoResponseDTO> agregarItem(@AuthenticationPrincipal Usuario usuario, @Valid @RequestBody AgregarItemCarritoRequestDTO request) {
        return ResponseEntity.ok(service.agregarItem(usuario, request));
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CarritoResponseDTO> actualizarCantidad(@AuthenticationPrincipal Usuario usuario, @PathVariable Long itemId, @Valid @RequestBody ActualizarCantidadItemRequestDTO request) {
        return ResponseEntity.ok(service.actualizarCantidad(usuario, itemId, request));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CarritoResponseDTO> eliminarItem(@AuthenticationPrincipal Usuario usuario, @PathVariable Long itemId) {
        return ResponseEntity.ok(service.eliminarItem(usuario, itemId));
    }
}
