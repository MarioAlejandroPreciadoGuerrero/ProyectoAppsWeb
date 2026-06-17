package com.example.E_commerce_Bemole;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    @GetMapping("/")
    public String inicio() {
        return "bemole_home_page";
    }
}
