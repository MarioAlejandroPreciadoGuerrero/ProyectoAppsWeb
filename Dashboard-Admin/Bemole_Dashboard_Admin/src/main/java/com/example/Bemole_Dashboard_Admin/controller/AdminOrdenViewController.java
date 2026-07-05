package com.example.Bemole_Dashboard_Admin.controller;

import com.example.Bemole_Dashboard_Admin.client.AdminOrdenApiClient;
import com.example.Bemole_Dashboard_Admin.enums.EstadoOrden;
import com.example.Bemole_Dashboard_Admin.dto.admin.orden.OrdenAdminResponseDTO;
import com.example.Bemole_Dashboard_Admin.dto.PaginacionDTO;
import com.example.Bemole_Dashboard_Admin.exception.ApiClientException;
import com.example.Bemole_Dashboard_Admin.session.AdminSessionService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/ordenes")
@AllArgsConstructor
public class AdminOrdenViewController {
    private final AdminOrdenApiClient ordenApiClient;
    private final AdminSessionService sessionService;

    @GetMapping
    public String listar(@RequestParam(required = false) EstadoOrden estado, @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "10") int tamano, HttpSession session, Model model) {
        pagina = Math.max(pagina, 0);

        if (tamano < 1 || tamano > 50) {
            tamano = 10;
        }

        PaginacionDTO<OrdenAdminResponseDTO> resultado = ordenApiClient.listar(sessionService.obtenerToken(session), estado, pagina, tamano);

        model.addAttribute("paginaOrdenes", resultado);

        model.addAttribute("ordenes", resultado != null ? resultado.getContent() : java.util.List.of());

        model.addAttribute("estadoSeleccionado", estado);

        model.addAttribute("estados", EstadoOrden.values());

        return "admin/ordenes/lista";
    }

    @PatchMapping("/{id}/estado")
    public String actualizarEstado(@PathVariable Long id, @RequestParam EstadoOrden estado,HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            ordenApiClient.actualizarEstado(sessionService.obtenerToken(session), id, estado);

            redirectAttributes.addFlashAttribute("mensaje", "El estado de la orden fue actualizado.");

        } catch (ApiClientException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }

        return "redirect:/admin/ordenes";
    }
}
