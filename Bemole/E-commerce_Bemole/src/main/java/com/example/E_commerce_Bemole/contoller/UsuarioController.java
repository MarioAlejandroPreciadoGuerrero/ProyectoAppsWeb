package com.example.E_commerce_Bemole.contoller;

import com.example.E_commerce_Bemole.dto.usuarios.LoginDTO;
import com.example.E_commerce_Bemole.dto.usuarios.UsuarioRegistroDTO;
import com.example.E_commerce_Bemole.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsuarioController {
    @Autowired
    private UsuarioService service;




}
