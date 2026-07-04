package com.example.Bemole_Dashboard_Admin.controller;

import com.example.Bemole_Dashboard_Admin.dto.admin.AdminSesionDTO;
import com.example.Bemole_Dashboard_Admin.session.AdminSessionKeys;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {
    @GetMapping("/")
    public String inicio() {
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String dashboard(HttpSession session, Model model) {
        AdminSesionDTO admin = (AdminSesionDTO) session.getAttribute(AdminSessionKeys.ADMIN_SESSION);

        model.addAttribute("adminSesion", admin);

        return "admin/dashboard";
    }
}
