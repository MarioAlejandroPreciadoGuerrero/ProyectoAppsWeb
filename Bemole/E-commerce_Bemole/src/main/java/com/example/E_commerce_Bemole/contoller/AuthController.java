package com.example.E_commerce_Bemole.contoller;

import com.example.E_commerce_Bemole.dto.AuthResponseDTO;
import com.example.E_commerce_Bemole.dto.LoginDTO;
import com.example.E_commerce_Bemole.service.AuthService;
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

    @GetMapping("/login")
    public String mostrarLogin(Model model){
        model.addAttribute("credenciales",new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO credenciales, HttpSession session){
        AuthResponseDTO response = service.login(credenciales);
        session.setAttribute("token", response.getToken());

        return "redirect:/";
    }
}
