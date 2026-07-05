package com.example.Bemole_Dashboard_Admin.controller;

import com.example.Bemole_Dashboard_Admin.client.AdminResumenApiClient;
import com.example.Bemole_Dashboard_Admin.dto.admin.AdminResumenDTO;
import com.example.Bemole_Dashboard_Admin.dto.admin.AdminSesionDTO;
import com.example.Bemole_Dashboard_Admin.session.AdminSessionKeys;
import com.example.Bemole_Dashboard_Admin.session.AdminSessionService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class AdminDashboardController {

    private final AdminResumenApiClient resumenApiClient;
    private final AdminSessionService sessionService;

    @GetMapping("/")
    public String inicio() {
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String dashboard(
            HttpSession session,
            Model model
    ) {
        AdminSesionDTO admin =
                sessionService.obtenerAdmin(session);

        AdminResumenDTO resumen = resumenApiClient.obtenerResumen(admin.getToken());

        model.addAttribute("adminSesion", admin);

        model.addAttribute("resumen", resumen);

        return "admin/dashboard";
    }
}
