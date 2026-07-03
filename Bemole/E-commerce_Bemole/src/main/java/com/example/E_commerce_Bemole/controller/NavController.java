package com.example.E_commerce_Bemole.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavController {


    @GetMapping("/")
    public String inicio() {
        return "home";
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

    @GetMapping("/ordenes")
    public String ordenes(){
        return "ordenes";
    }
}
