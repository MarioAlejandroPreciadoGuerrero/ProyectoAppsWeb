package com.example.E_commerce_Bemole.controller;

import com.example.E_commerce_Bemole.client.PagoApiClient;
import com.example.E_commerce_Bemole.dto.pago.CrearPagoRequestDTO;
import com.example.E_commerce_Bemole.dto.pago.CrearPagoResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import com.example.E_commerce_Bemole.client.CarritoApiClient;
import com.example.E_commerce_Bemole.client.OrdenApiClient;
import com.example.E_commerce_Bemole.dto.usuarios.UsuarioSesionDTO;
import com.example.E_commerce_Bemole.dto.carrito.CarritoResponseDTO;
import com.example.E_commerce_Bemole.dto.error.ApiErrorDTO;
import com.example.E_commerce_Bemole.dto.ordenes.CrearOrdenFormDTO;
import com.example.E_commerce_Bemole.enums.MetodoEnvio;
import com.example.E_commerce_Bemole.dto.ordenes.response.OrdenCreadaResponseDTO;
import com.example.E_commerce_Bemole.exception.ApiClientException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class CheckoutViewController {
    private final CarritoApiClient carritoApiClient;
    private final OrdenApiClient ordenApiClient;
    private final PagoApiClient pagoApiClient;

    @GetMapping("/checkout")
    public String mostrarCheckout(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String token = obtenerToken(session);

        try {
            CarritoResponseDTO carrito = carritoApiClient.obtenerCarrito(token);

            if (carrito == null || carrito.getItems().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorCarrito", "Agrega productos antes de continuar al checkout.");

                return "redirect:/carrito";
            }

            if (!model.containsAttribute("checkout")) {
                model.addAttribute("checkout", crearFormularioInicial(session));
            }

            model.addAttribute("carrito", carrito);
            model.addAttribute("metodosEnvio", MetodoEnvio.values());

            return "checkout";

        } catch (ApiClientException exception) {
            if (esNoAutorizado(exception)) {
                session.invalidate();
                return "redirect:/login?sesionExpirada";
            }

            redirectAttributes.addFlashAttribute("errorCarrito", obtenerMensaje(exception));

            return "redirect:/carrito";

        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorCarrito", "No fue posible iniciar el checkout.");

            return "redirect:/carrito";
        }
    }

    /*
    * @PostMapping("/checkout")
    public String crearOrden(@Valid @ModelAttribute("checkout") CrearOrdenFormDTO checkout, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String token = obtenerToken(session);

        if (bindingResult.hasErrors()) {
            cargarDatosCheckout(token, model);
            return "checkout";
        }

        try {
            OrdenCreadaResponseDTO ordenCreada = ordenApiClient.crearOrden(token, checkout);

            redirectAttributes.addFlashAttribute("ordenCreada", ordenCreada);

            return "redirect:/orden-confirmacion";

        } catch (ApiClientException exception) {
            if (esNoAutorizado(exception)) {
                session.invalidate();
                return "redirect:/login?sesionExpirada";
            }

            cargarDatosCheckout(token, model);

            model.addAttribute("errorCheckout", obtenerMensaje(exception));

            return "checkout";

        } catch (Exception exception) {
            cargarDatosCheckout(token, model);

            model.addAttribute("errorCheckout", "No fue posible crear la orden.");

            return "checkout";
        }
    }
    * */

    @GetMapping("/orden-confirmacion")
    public String mostrarConfirmacion(Model model) {
        if (!model.containsAttribute("ordenCreada")) {
            return "redirect:/ordenes";
        }

        return "orden-confirmacion";
    }

    private void cargarDatosCheckout(String token, Model model) {
        try {
            CarritoResponseDTO carrito = carritoApiClient.obtenerCarrito(token);

            model.addAttribute("carrito", carrito);

        } catch (Exception exception) {
            model.addAttribute("carrito", new CarritoResponseDTO());
        }

        model.addAttribute("metodosEnvio", MetodoEnvio.values());
    }

    private CrearOrdenFormDTO crearFormularioInicial(HttpSession session) {
        CrearOrdenFormDTO formulario = new CrearOrdenFormDTO();

        Object atributoUsuario = session.getAttribute(AuthViewController.SESSION_USUARIO);

        if (atributoUsuario instanceof UsuarioSesionDTO usuario) {
            formulario.getContacto().setNombre(usuario.getNombre());

            formulario.getContacto().setApellido(usuario.getApellido());

            formulario.getContacto().setEmail(usuario.getEmail());
        }

        formulario.setMetodoEnvio(MetodoEnvio.ESTANDAR);

        return formulario;
    }

    @PostMapping("/checkout/pagar")
    public String pagarConMercadoPago(@ModelAttribute("checkout") CrearOrdenFormDTO checkout,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {

        String token = obtenerToken(session);

        if (token == null || token.isBlank()) {
            redirectAttributes.addFlashAttribute("errorCheckout", "Debes iniciar sesión para realizar el pago.");
            return "redirect:/login";
        }

        try {
            OrdenCreadaResponseDTO orden = ordenApiClient.crearOrden(token, checkout);

            CrearPagoRequestDTO pagoRequest = new CrearPagoRequestDTO();
            pagoRequest.setOrdenId(orden.getId());

            CrearPagoResponseDTO pagoResponse = pagoApiClient.crearPago(token, pagoRequest);

            if (pagoResponse == null || pagoResponse.getInitPoint() == null || pagoResponse.getInitPoint().isBlank()) {
                redirectAttributes.addFlashAttribute("errorCheckout", "No fue posible iniciar el pago con Mercado Pago."
                );
                return "redirect:/checkout";
            }

            return "redirect:" + pagoResponse.getInitPoint();

        } catch (ApiClientException e) {
            e.printStackTrace();

            redirectAttributes.addFlashAttribute("errorCheckout", e.getMessage());

            return "redirect:/checkout";

        } catch (Exception e) {
            e.printStackTrace();

            redirectAttributes.addFlashAttribute("errorCheckout", "Ocurrió un error al iniciar el pago.");

            return "redirect:/checkout";
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
            return "No fue posible completar la compra.";
        }

        return error.getMensaje();
    }
}
