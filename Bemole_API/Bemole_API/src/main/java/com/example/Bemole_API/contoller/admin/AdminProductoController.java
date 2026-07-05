package com.example.Bemole_API.contoller.admin;

import com.example.Bemole_API.dto.admin.request.ActualizarActivoProductoDTO;
import lombok.AllArgsConstructor;
import com.example.Bemole_API.dto.admin.request.ProductoAdminRequestDTO;
import com.example.Bemole_API.dto.admin.response.ProductoAdminResponseDTO;
import com.example.Bemole_API.service.admin.AdminProductoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/productos")
@AllArgsConstructor
public class AdminProductoController {
    private final AdminProductoService service;

    /*Solo CRUD basico*/
    @GetMapping
    public Page<ProductoAdminResponseDTO> listar(@PageableDefault(size = 10, sort = "nombre") Pageable pageable) {
        return service.listarProductos(pageable);
    }

    @GetMapping("/{id}")
    public ProductoAdminResponseDTO obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<ProductoAdminResponseDTO> crear(@Valid @RequestBody ProductoAdminRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearProducto(request));
    }

    @PutMapping("/{id}")
    public ProductoAdminResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProductoAdminRequestDTO request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activo")
    public ProductoAdminResponseDTO actualizarActivo(@PathVariable Long id, @Valid @RequestBody ActualizarActivoProductoDTO request) {
        return service.actualizarActivo(id, request.getActivo());
    }
}
