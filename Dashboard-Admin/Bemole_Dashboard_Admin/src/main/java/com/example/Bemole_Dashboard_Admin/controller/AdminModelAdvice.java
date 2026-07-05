package com.example.Bemole_Dashboard_Admin.controller;

import com.example.Bemole_Dashboard_Admin.dto.admin.AdminSesionDTO;
import com.example.Bemole_Dashboard_Admin.session.AdminSessionKeys;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class AdminModelAdvice {

    @ModelAttribute("adminSesion")
    public AdminSesionDTO agregarAdminSesion(HttpSession session) {
        Object atributo = session.getAttribute(AdminSessionKeys.ADMIN_SESSION);

        if (atributo instanceof AdminSesionDTO admin) {
            return admin;
        }

        return null;
    }
}
