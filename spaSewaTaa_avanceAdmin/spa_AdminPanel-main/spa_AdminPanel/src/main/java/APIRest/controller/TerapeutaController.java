package APIRest.controller;

import APIRest.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/terapeutas")
public class TerapeutaController {

    @Autowired
    private ServicioService servicioService;

    
}
