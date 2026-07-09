package com.example.E_commerce_Bemole.controller;

import com.example.E_commerce_Bemole.dto.ordenes.response.OrdenDetalleResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import com.example.E_commerce_Bemole.client.OrdenApiClient;
import com.example.E_commerce_Bemole.dto.usuarios.UsuarioSesionDTO;
import com.example.E_commerce_Bemole.dto.PaginacionDTO;
import com.example.E_commerce_Bemole.dto.error.ApiErrorDTO;
import com.example.E_commerce_Bemole.enums.EstadoOrden;
import com.example.E_commerce_Bemole.dto.ordenes.response.OrdenResumenResponseDTO;
import com.example.E_commerce_Bemole.exception.ApiClientException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class OrdenViewController {
    private final OrdenApiClient ordenApiClient;

    @GetMapping("/ordenes")
    public String mostrarOrdenes(@RequestParam(required = false) String estado, @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "10") int tamano, HttpSession session, Model model) {
        Object tokenAtributo = session.getAttribute(AuthViewController.SESSION_TOKEN);

        if (tokenAtributo == null) {
            return "redirect:/login";
        }

        EstadoOrden estadoSeleccionado = convertirEstado(estado);

        corregirPaginacion(model, pagina, tamano);

        pagina = pagina < 0 ? 0 : pagina;
        tamano = tamano < 1 || tamano > 50 ? 10 : tamano;

        try {
            PaginacionDTO<OrdenResumenResponseDTO> resultado = ordenApiClient.listarOrdenes(
                    tokenAtributo.toString(),
                    estadoSeleccionado,
                    pagina,
                    tamano
            );

            model.addAttribute("paginaOrdenes", resultado);

            model.addAttribute("ordenes", resultado != null && resultado.getContenido() != null ? resultado.getContenido() : List.of());

            model.addAttribute("estadoSeleccionado", estadoSeleccionado);

            model.addAttribute("tamanoSeleccionado", tamano);

            cargarUsuario(session, model);

            return "ordenes";

        } catch (ApiClientException exception) {
            if (esNoAutorizado(exception)) {
                session.invalidate();

                return "redirect:/login?sesionExpirada";
            }

            model.addAttribute("ordenes", List.of());

            model.addAttribute("errorOrdenes", obtenerMensaje(exception));

            model.addAttribute("estadoSeleccionado", estadoSeleccionado);

            cargarUsuario(session, model);

            return "ordenes";

        } catch (Exception exception) {
            model.addAttribute("ordenes", List.of());

            model.addAttribute("errorOrdenes", "No fue posible cargar el historial de órdenes.");

            model.addAttribute("estadoSeleccionado", estadoSeleccionado);

            cargarUsuario(session, model);

            return "ordenes";
        }
    }

    @GetMapping("/ordenes/{id}")
    public String verDetalleOrden(@PathVariable Long id, HttpSession session, Model model) {
        Object token = session.getAttribute(AuthViewController.SESSION_TOKEN);

        String tokenString = token.toString();

        try {
            OrdenDetalleResponseDTO orden = ordenApiClient.obtenerDetalleOrden(tokenString, id);

            model.addAttribute("orden", orden);

            cargarUsuario(session, model);

            return "ordenes/detalle";

        } catch (ApiClientException exception) {
            model.addAttribute("errorOrdenDetalle", exception.getMessage());

            cargarUsuario(session, model);

            return "ordenes/detalle";

        } catch (Exception exception) {
            model.addAttribute("errorOrdenDetalle", "No fue posible cargar el detalle de la orden.");

            cargarUsuario(session, model);

            return "ordenes/detalle";
        }
    }

    private EstadoOrden convertirEstado(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        try {
            return EstadoOrden.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private void corregirPaginacion(Model model, int pagina, int tamano) {
        if (pagina < 0) {
            model.addAttribute("errorOrdenes", "La página indicada no es válida.");
        }

        if (tamano < 1 || tamano > 50) {
            model.addAttribute("errorOrdenes", "El tamaño de página no es válido.");
        }
    }

    private void cargarUsuario(HttpSession session, Model model) {
        Object atributo = session.getAttribute(AuthViewController.SESSION_USUARIO);

        if (!(atributo instanceof UsuarioSesionDTO usuario)) {
            return;
        }

        model.addAttribute("usuarioOrdenes", usuario);

        String inicial = "U";

        if (usuario.getNombre() != null && !usuario.getNombre().isBlank()) {
            inicial = usuario
                    .getNombre()
                    .substring(0, 1)
                    .toUpperCase()
            ;
        }

        model.addAttribute("inicialUsuario", inicial);
    }

    private boolean esNoAutorizado(ApiClientException exception) {
        return exception.getError() != null && exception.getError().getStatus() == 401;
    }

    private String obtenerMensaje(ApiClientException exception) {
        ApiErrorDTO error = exception.getError();

        if (error == null || error.getMensaje() == null || error.getMensaje().isBlank()) {
            return "No fue posible consultar las órdenes.";
        }

        return error.getMensaje();
    }
}
