package com.example.E_commerce_Bemole.controller;

import com.example.E_commerce_Bemole.client.CarritoApiClient;
import com.example.E_commerce_Bemole.dto.carrito.ActualizarCantidadItemFormDTO;
import com.example.E_commerce_Bemole.dto.carrito.AgregarItemCarritoFormDTO;
import com.example.E_commerce_Bemole.dto.carrito.CarritoResponseDTO;
import com.example.E_commerce_Bemole.dto.error.ApiErrorDTO;
import com.example.E_commerce_Bemole.exception.ApiClientException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class CarritoViewController {

    private final CarritoApiClient carritoApiClient;

    @GetMapping("/carrito")
    public String mostrarCarrito(HttpSession session, Model model) {
        String token = obtenerToken(session);

        try {
            CarritoResponseDTO carrito = carritoApiClient.obtenerCarrito(token);

            model.addAttribute("carrito", carrito);

            return "carrito";

        } catch (ApiClientException exception) {
            if (esNoAutorizado(exception)) {
                session.invalidate();
                return "redirect:/login?sesionExpirada";
            }

            model.addAttribute("carrito", new CarritoResponseDTO());

            model.addAttribute("errorCarrito", obtenerMensaje(exception));

            return "carrito";

        } catch (Exception exception) {
            model.addAttribute("carrito", new CarritoResponseDTO());

            model.addAttribute("errorCarrito", "No fue posible cargar el carrito.");

            return "carrito";
        }
    }

    @PostMapping("/carrito/items")
    public String agregarItem(@Valid @ModelAttribute AgregarItemCarritoFormDTO formulario, BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorCarrito", obtenerPrimerError(bindingResult));

            return "redirect:/carrito";
        }

        try {
            carritoApiClient.agregarItem(obtenerToken(session), formulario);

            redirectAttributes.addFlashAttribute("mensajeCarrito", "El producto se agregó al carrito.");

            return "redirect:/carrito";

        } catch (ApiClientException exception) {
            return manejarErrorApi(exception, session, redirectAttributes);

        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorCarrito", "No fue posible agregar el producto.");

            return "redirect:/carrito";
        }
    }

    @PatchMapping("/carrito/items/{itemId}")
    public String actualizarCantidad(@PathVariable Long itemId, @Valid @ModelAttribute ActualizarCantidadItemFormDTO formulario, BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorCarrito", obtenerPrimerError(bindingResult));

            return "redirect:/carrito";
        }

        try {
            carritoApiClient.actualizarCantidad(obtenerToken(session), itemId, formulario);

            redirectAttributes.addFlashAttribute("mensajeCarrito", "La cantidad fue actualizada.");

            return "redirect:/carrito";

        } catch (ApiClientException exception) {
            return manejarErrorApi(exception, session, redirectAttributes);

        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorCarrito", "No fue posible modificar la cantidad.");

            return "redirect:/carrito";
        }
    }

    @DeleteMapping("/carrito/items/{itemId}")
    public String eliminarItem(@PathVariable Long itemId, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            carritoApiClient.eliminarItem(obtenerToken(session), itemId);

            redirectAttributes.addFlashAttribute("mensajeCarrito", "El producto fue eliminado del carrito.");

            return "redirect:/carrito";

        } catch (ApiClientException exception) {
            return manejarErrorApi(exception, session, redirectAttributes);

        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorCarrito", "No fue posible eliminar el producto.");

            return "redirect:/carrito";
        }
    }

    private String obtenerToken(HttpSession session) {
        Object token = session.getAttribute(AuthViewController.SESSION_TOKEN);

        if (token == null) {
            throw new IllegalStateException("No existe una sesión autenticada.");
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

    private String manejarErrorApi(ApiClientException exception, HttpSession session, RedirectAttributes redirectAttributes) {
        if (esNoAutorizado(exception)) {
            session.invalidate();
            return "redirect:/login?sesionExpirada";
        }

        redirectAttributes.addFlashAttribute("errorCarrito", obtenerMensaje(exception));

        return "redirect:/carrito";
    }

    private String obtenerPrimerError(BindingResult bindingResult) {
        if (bindingResult.getFieldError() == null) {
            return "Los datos enviados no son válidos.";
        }

        String mensaje = bindingResult.getFieldError().getDefaultMessage();

        return mensaje != null ? mensaje : "Los datos enviados no son válidos.";
    }
}
