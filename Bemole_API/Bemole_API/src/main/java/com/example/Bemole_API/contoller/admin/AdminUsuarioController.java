package com.example.Bemole_API.contoller.admin;

import com.example.Bemole_API.dto.admin.response.UsuarioAdminResponseDTO;
import com.example.Bemole_API.service.admin.AdminUsuariosService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/usuarios")
@AllArgsConstructor
public class AdminUsuarioController {
    private final AdminUsuariosService service;

    @GetMapping
    public Page<UsuarioAdminResponseDTO> listar(@PageableDefault(size = 10, sort = "fechaRegistro") Pageable pageable) {
        return service.listar(pageable);
    }
}
