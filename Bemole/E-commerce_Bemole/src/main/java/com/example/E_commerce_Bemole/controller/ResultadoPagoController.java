package com.example.E_commerce_Bemole.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResultadoPagoController {

    @GetMapping("/pago/exito")
    public String pagoExito(@RequestParam Long ordenId) {
        return "pago/exito";
    }

    @GetMapping("/pago/pendiente")
    public String pagoPendiente(@RequestParam Long ordenId) {
        return "pago/pendiente";
    }

    @GetMapping("/pago/error")
    public String pagoError(@RequestParam Long ordenId) {
        return "pago/error";
    }
}
