package com.example.Bemole_API.contoller.admin;

import com.example.Bemole_API.dto.admin.AdminResumenDTO;
import com.example.Bemole_API.service.admin.AdminResumenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/resumen")
@AllArgsConstructor
public class AdminResumenController {
    private final AdminResumenService service;

    @GetMapping
    public AdminResumenDTO obtenerResumen() {
        return service.obtenerResumen();
    }
}
