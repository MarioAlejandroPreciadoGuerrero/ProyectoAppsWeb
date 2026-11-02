package com.example.spa_AdminPanel.controller;

import com.example.spa_AdminPanel.model.Cita;
import com.example.spa_AdminPanel.model.EstadoCita;
import com.example.spa_AdminPanel.model.Servicio;
import com.example.spa_AdminPanel.model.Terapeuta;
import com.example.spa_AdminPanel.service.CitaService;
import com.example.spa_AdminPanel.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private ServicioService servicioService;

    // Se agregan las múltiples rutas para evitar el error 404
    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        List<Cita> todasLasCitas = citaService.obtenerCitas();

        // Conteo de métricas reales a partir de las citas registradas
        long confirmadas = todasLasCitas.stream()
                .filter(c -> c.getEstado() == EstadoCita.CONFIRMADA)
                .count();

        long canceladas = todasLasCitas.stream()
                .filter(c -> c.getEstado() == EstadoCita.CANCELADA)
                .count();

        long pendientes = todasLasCitas.stream()
                .filter(c -> c.getEstado() == EstadoCita.PENDIENTE)
                .count();

        long total = todasLasCitas.size();
        int porcentajeOcupacion = total > 0 ? (int) Math.round(((double) (confirmadas + pendientes) / total) * 100) : 0;

        // Mandar los datos al modelo
        model.addAttribute("porcentajeOcupacion", porcentajeOcupacion);
        model.addAttribute("totalConfirmadas", confirmadas);
        model.addAttribute("totalCanceladas", canceladas);
        model.addAttribute("serviciosPopulares", servicioService.obtenerServicios());

        return "admin/dashboard";
    }

    @GetMapping("/servicios")
    public String servicios(Model model) {
        // Enviamos un objeto vacío para el formulario (th:object="${servicio}")
        model.addAttribute("servicio", new Servicio());
        // Si también muestras una tabla con la lista de servicios, agrégala:
        model.addAttribute("servicios", servicioService.obtenerServicios());
        return "admin/servicios";
    }

    @GetMapping("/horarios")
    public String horarios(Model model) {
        // Enviamos un objeto vacío para el formulario (th:object="${terapeuta}")
        model.addAttribute("terapeuta", new Terapeuta()); // Asegúrate de tener la clase Terapeuta creada
        return "admin/horarios";
    }

    @GetMapping("/agenda")
    public String agenda() {
        return "admin/agenda";
    }

    @GetMapping("/reservaciones/nueva")
    public String nuevaReserva() {
        return "admin/nueva_reserva";
    }

    @GetMapping("/clientes")
    public String clientes() {
        return "admin/clientes";
    }
}