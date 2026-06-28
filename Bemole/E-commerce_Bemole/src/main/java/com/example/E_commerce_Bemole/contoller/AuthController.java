package com.example.E_commerce_Bemole.contoller;

import com.example.E_commerce_Bemole.dto.usuarios.AuthResponseDTO;
import com.example.E_commerce_Bemole.dto.usuarios.LoginDTO;
import com.example.E_commerce_Bemole.dto.usuarios.UsuarioRegistroDTO;
import com.example.E_commerce_Bemole.service.AuthService;
import com.example.E_commerce_Bemole.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @Autowired
    private AuthService service;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin(Model model){
        model.addAttribute("credenciales",new LoginDTO());
        model.addAttribute("usuarioDTO", new UsuarioRegistroDTO());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO credenciales, HttpSession session){
        AuthResponseDTO response = service.login(credenciales);
        session.setAttribute("token", response.getToken());

        return "redirect:/";
    }

    @PostMapping("/registro")
    public String registro(@ModelAttribute UsuarioRegistroDTO usuarioNuevo) {

        usuarioNuevo.setRol("CLIENTE");

        usuarioService.registrar(usuarioNuevo);

        return "redirect:/login";
    }

}
