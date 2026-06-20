package com.example.E_commerce_Bemole.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavController {


    @GetMapping("/" )
    public String inicio() {
        return "home_page";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/catalogo")
    public String catalogo() {
        return "catalogo";
    }

    @GetMapping("/carrito")
    public String carrito() {
        return "carrito";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }

    @GetMapping("/producto")
    public String productos() {
        return "producto";
    }
}
