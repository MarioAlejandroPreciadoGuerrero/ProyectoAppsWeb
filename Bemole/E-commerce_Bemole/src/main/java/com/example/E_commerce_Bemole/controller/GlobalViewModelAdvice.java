package com.example.E_commerce_Bemole.controller;

import com.example.E_commerce_Bemole.dto.usuarios.UsuarioSesionDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalViewModelAdvice {
    @ModelAttribute("usuarioSesion")
    public UsuarioSesionDTO usuarioSesion(HttpSession session) {
        return (UsuarioSesionDTO) session.getAttribute(AuthViewController.SESSION_USUARIO);
    }

    @ModelAttribute("autenticado")
    public boolean autenticado(HttpSession session) {
        return session.getAttribute(AuthViewController.SESSION_TOKEN) != null;
    }
}
