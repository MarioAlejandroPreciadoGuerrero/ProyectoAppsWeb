package com.example.Bemole_API.contoller.admin;

import com.example.Bemole_API.dto.admin.request.CategoriaAdminRequestDTO;
import com.example.Bemole_API.dto.admin.response.CategoriaAdminResponseDTO;
import com.example.Bemole_API.service.admin.AdminCategoriaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categorias")
@AllArgsConstructor
public class AdminCategoriaController {
    private final AdminCategoriaService service;

    @GetMapping
    public List<CategoriaAdminResponseDTO> listar() {
        return service.listarCategorias();
    }

    @PostMapping
    public ResponseEntity<CategoriaAdminResponseDTO> crear(@Valid @RequestBody CategoriaAdminRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearCategoria(request));
    }

    @PutMapping("/{id}")
    public CategoriaAdminResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaAdminRequestDTO request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
