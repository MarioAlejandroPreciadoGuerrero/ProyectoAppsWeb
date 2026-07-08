package com.example.E_commerce_Bemole.controller;

import com.example.E_commerce_Bemole.client.PagoApiClient;
import com.example.E_commerce_Bemole.dto.pago.CrearPagoRequestDTO;
import com.example.E_commerce_Bemole.dto.pago.CrearPagoResponseDTO;
import com.example.E_commerce_Bemole.exception.ApiClientException;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pagos")
@AllArgsConstructor
public class PagoController {
    private final PagoApiClient pagoApiClient;

    @PostMapping("/crear")
    public String crearPago(@RequestParam Long ordenId, HttpSession session, RedirectAttributes redirectAttributes) {

        String token = (String) session.getAttribute("token");

        if (token == null || token.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para realizar el pago.");
            return "redirect:/login";
        }

        try {
            CrearPagoRequestDTO request = new CrearPagoRequestDTO();
            request.setOrdenId(ordenId);

            CrearPagoResponseDTO response = pagoApiClient.crearPago(token, request);

            if (response == null || response.getInitPoint() == null || response.getInitPoint().isBlank()) {
                redirectAttributes.addFlashAttribute("error", "No fue posible iniciar el pago. Intenta nuevamente.");
                return "redirect:/checkout";
            }

            return "redirect:" + response.getInitPoint();

        } catch (ApiClientException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al iniciar el pago.");
            return "redirect:/checkout";
        }
    }
}
