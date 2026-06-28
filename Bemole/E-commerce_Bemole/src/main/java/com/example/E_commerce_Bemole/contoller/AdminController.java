package com.example.E_commerce_Bemole.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping({"","/"})
    public String dashboard(){
        return "admin/dashboard";
    }

    @GetMapping("/productos")
    public String productos(){
        return "admin/productos";
    }

    @GetMapping("/categorias")
    public String categorias(){
        return "admin/categorias";
    }

    @GetMapping("/usuarios")
    public String usuarios(){
        return "admin/usuarios";
    }
}
