package com.example.E_commerce_Bemole.controller;

import com.example.E_commerce_Bemole.client.AuthApiClient;
import com.example.E_commerce_Bemole.dto.usuarios.LoginFormDTO;
import com.example.E_commerce_Bemole.dto.usuarios.RegistroFormDTO;
import com.example.E_commerce_Bemole.dto.usuarios.SesionResponseDTO;
import com.example.E_commerce_Bemole.dto.error.ApiErrorDTO;
import com.example.E_commerce_Bemole.exception.ApiClientException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class AuthViewController {
    public static final String SESSION_TOKEN = "jwtToken";
    public static final String SESSION_USUARIO = "usuarioSesion";

    private final AuthApiClient authApiClient;

    @GetMapping("/login")
    public String mostrarLogin(Model model, HttpSession session) {
        if (session.getAttribute(SESSION_TOKEN) != null) {
            return "redirect:/";
        }

        if (!model.containsAttribute("credenciales")) {
            model.addAttribute("credenciales", new LoginFormDTO());
        }

        if (!model.containsAttribute("registro")) {
            model.addAttribute("registro", new RegistroFormDTO());
        }

        return "login";
    }

    @PostMapping("/login")
    public String iniciarSesion(@Valid @ModelAttribute("credenciales") LoginFormDTO credenciales, BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("registro", new RegistroFormDTO());

            model.addAttribute("panelActivo", "login");

            return "login";
        }

        try {
            SesionResponseDTO respuesta = authApiClient.iniciarSesion(credenciales);

            session.setAttribute(SESSION_TOKEN, respuesta.getToken()
            );

            session.setAttribute(SESSION_USUARIO, respuesta.getUsuario());

            session.setMaxInactiveInterval(Math.toIntExact(respuesta.getExpiraEn()));

            return "redirect:/";

        } catch (ApiClientException exception) {
            ApiErrorDTO error = exception.getError();

            model.addAttribute("registro", new RegistroFormDTO());

            model.addAttribute("errorLogin", error != null ? error.getMensaje() : "No fue posible iniciar sesión.");

            model.addAttribute("panelActivo", "login");

            return "login";
        } catch (Exception exception) {
            model.addAttribute("registro", new RegistroFormDTO());

            model.addAttribute("errorLogin", "No fue posible comunicarse con el servidor.");

            model.addAttribute("panelActivo", "login");

            return "login";
        }
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("registro") RegistroFormDTO registro, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("credenciales", new LoginFormDTO());

            model.addAttribute("panelActivo", "registro");

            return "login";
        }

        try {
            authApiClient.registrar(registro);

            return "redirect:/login?registroExitoso";

        } catch (ApiClientException exception) {
            ApiErrorDTO error = exception.getError();

            model.addAttribute("credenciales", new LoginFormDTO());

            model.addAttribute("errorRegistro", error != null ? error.getMensaje() : "No fue posible crear la cuenta.");

            if (error != null && error.getCampos() != null) {
                model.addAttribute("erroresApi", error.getCampos());
            }

            model.addAttribute("panelActivo", "registro");

            return "login";

        } catch (Exception exception) {
            model.addAttribute("credenciales", new LoginFormDTO());

            model.addAttribute("errorRegistro", "No fue posible comunicarse con el servidor.");

            model.addAttribute("panelActivo", "registro");

            return "login";
        }
    }

    @PostMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
}
