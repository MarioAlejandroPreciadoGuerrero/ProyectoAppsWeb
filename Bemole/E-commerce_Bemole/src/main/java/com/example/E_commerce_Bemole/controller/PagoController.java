package com.example.E_commerce_Bemole.controller;

import com.example.E_commerce_Bemole.client.PagoApiClient;
import com.example.E_commerce_Bemole.dto.error.ApiErrorDTO;
import com.example.E_commerce_Bemole.dto.pago.CrearPagoRequestDTO;
import com.example.E_commerce_Bemole.dto.pago.CrearPagoResponseDTO;
import com.example.E_commerce_Bemole.exception.ApiClientException;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pago")
@AllArgsConstructor
public class PagoController {
    private final PagoApiClient pagoApiClient;

    @PostMapping("/crear")
    public String crearPago(@RequestParam Long ordenId, HttpSession session, RedirectAttributes redirectAttributes) {

        String token = obtenerToken(session);

        if (token == null || token.isBlank() ) {
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

    @GetMapping("/exito")
    public String exito(
            @RequestParam(name = "payment_id", required = false)
            String paymentId,

            @RequestParam(name = "collection_id", required = false)
            String collectionId,

            @RequestParam(name = "status", required = false)
            String status,

            @RequestParam(name = "preference_id", required = false)
            String preferenceId,

            @RequestParam(name = "external_reference", required = false)
            String externalReference,

            @RequestParam(name = "ordenId", required = false)
            String ordenId,

            HttpSession session,
            Model model
    ) {
        String token = obtenerToken(session);

        String paymentIdReal =
                paymentId != null && !paymentId.isBlank()
                        ? paymentId
                        : collectionId;

        String referenciaOrden =
                externalReference != null && !externalReference.isBlank()
                        ? externalReference
                        : ordenId;

        try {
            pagoApiClient.procesarRetornoMercadoPago(
                    token,
                    paymentIdReal,
                    status,
                    preferenceId,
                    referenciaOrden
            );

            model.addAttribute(
                    "mensajePago",
                    "Tu pago fue aprobado correctamente."
            );

        } catch (Exception exception) {
            model.addAttribute(
                    "errorPago",
                    "El pago fue aprobado en Mercado Pago, pero no pudimos actualizar la orden automáticamente."
            );
        }

        model.addAttribute("paymentId", paymentIdReal);
        model.addAttribute("status", status);
        model.addAttribute("preferenceId", preferenceId);
        model.addAttribute("ordenId", referenciaOrden);

        return "pago/exito";
    }

    @GetMapping("/pendiente")
    public String pendiente(
            @RequestParam(name = "payment_id", required = false)
            String paymentId,

            @RequestParam(name = "collection_id", required = false)
            String collectionId,

            @RequestParam(name = "status", required = false)
            String status,

            @RequestParam(name = "ordenId", required = false)
            String ordenId,

            Model model
    ) {
        model.addAttribute(
                "paymentId",
                paymentId != null ? paymentId : collectionId
        );

        model.addAttribute("status", status);
        model.addAttribute("ordenId", ordenId);

        return "pago/pendiente";
    }

    @GetMapping("/error")
    public String error(
            @RequestParam(name = "payment_id", required = false)
            String paymentId,

            @RequestParam(name = "collection_id", required = false)
            String collectionId,

            @RequestParam(name = "status", required = false)
            String status,

            @RequestParam(name = "ordenId", required = false)
            String ordenId,

            Model model
    ) {
        model.addAttribute(
                "paymentId",
                paymentId != null ? paymentId : collectionId
        );

        model.addAttribute("status", status);
        model.addAttribute("ordenId", ordenId);

        return "pago/error";
    }

    private String obtenerToken(HttpSession session) {
        Object token =
                session.getAttribute(
                        AuthViewController.SESSION_TOKEN
                );

        if (token == null) {
            throw new IllegalStateException(
                    "No existe una sesión autenticada."
            );
        }

        return token.toString();
    }

    private boolean esNoAutorizado(ApiClientException exception) {
        return exception.getError() != null && exception.getError().getStatus() == 401;
    }

    private String obtenerMensaje(ApiClientException exception) {
        ApiErrorDTO error = exception.getError();

        if (error == null || error.getMensaje() == null || error.getMensaje().isBlank()) {
            return "No fue posible completar la operación.";
        }

        return error.getMensaje();
    }
}
