package com.example.Bemole_Dashboard_Admin.controller;

import com.example.Bemole_Dashboard_Admin.client.AdminAuthApiClient;
import com.example.Bemole_Dashboard_Admin.dto.admin.AdminSesionDTO;
import com.example.Bemole_Dashboard_Admin.dto.auth.AdminLoginFormDTO;
import com.example.Bemole_Dashboard_Admin.dto.auth.UsuarioSesionDTO;
import com.example.Bemole_Dashboard_Admin.dto.auth.response.SesionResponseDTO;
import com.example.Bemole_Dashboard_Admin.exception.ApiClientException;
import com.example.Bemole_Dashboard_Admin.session.AdminSessionKeys;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminAuthController {
    @Autowired
    private final AdminAuthApiClient authApiClient;

    public AdminAuthController(
            AdminAuthApiClient authApiClient
    ) {
        this.authApiClient = authApiClient;
    }

    @GetMapping("/login")
    public String mostrarLogin(
            HttpSession session,
            Model model
    ) {
        if (session.getAttribute(
                AdminSessionKeys.ADMIN_SESSION
        ) != null) {
            return "redirect:/admin";
        }

        if (!model.containsAttribute("credenciales")) {
            model.addAttribute(
                    "credenciales",
                    new AdminLoginFormDTO()
            );
        }

        return "login";
    }

    @PostMapping("/login")
    public String iniciarSesion(@Valid AdminLoginFormDTO credenciales, BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("credenciales", credenciales);

            return "login";
        }

        try {
            SesionResponseDTO respuesta = authApiClient.iniciarSesion(credenciales);

            validarRespuesta(respuesta);

            UsuarioSesionDTO usuario = respuesta.getUsuario();

            if (!usuario.esAdministrador()) {
                model.addAttribute("credenciales", credenciales);

                model.addAttribute("errorLogin", "Tu cuenta no tiene permisos de administrador.");

                return "login";
            }

            AdminSesionDTO adminSesion = construirSesion(respuesta);

            session.setAttribute(AdminSessionKeys.ADMIN_SESSION, adminSesion);

            return "redirect:/admin";

        } catch (ApiClientException exception) {
            model.addAttribute("credenciales", credenciales);

            model.addAttribute("errorLogin", exception.getMessage());

            return "login";

        } catch (Exception exception) {
            model.addAttribute("credenciales", credenciales);

            model.addAttribute("errorLogin", "No fue posible iniciar sesión en el panel administrativo.");

            return "login";
        }
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado";
    }

    private void validarRespuesta(SesionResponseDTO respuesta) {
        if (respuesta == null || respuesta.getToken() == null || respuesta.getToken().isBlank() || respuesta.getUsuario() == null) {
            throw new IllegalStateException("La API devolvió una sesión incompleta.");
        }
    }

    private AdminSesionDTO construirSesion(SesionResponseDTO respuesta) {
        UsuarioSesionDTO usuario = respuesta.getUsuario();

        AdminSesionDTO sesion = new AdminSesionDTO();

        sesion.setUsuarioId(usuario.getId());
        sesion.setNombre(usuario.getNombre());
        sesion.setApellido(usuario.getApellido());
        sesion.setEmail(usuario.getEmail());
        sesion.setRol(usuario.getRol());
        sesion.setToken(respuesta.getToken());

        return sesion;
    }
}
