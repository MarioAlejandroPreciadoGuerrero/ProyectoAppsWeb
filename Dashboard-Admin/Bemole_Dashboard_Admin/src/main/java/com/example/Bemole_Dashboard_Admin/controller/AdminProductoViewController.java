package com.example.Bemole_Dashboard_Admin.controller;

import com.example.Bemole_Dashboard_Admin.client.AdminCategoriaApiClient;
import com.example.Bemole_Dashboard_Admin.client.AdminProductoApiClient;
import com.example.Bemole_Dashboard_Admin.dto.PaginacionDTO;
import com.example.Bemole_Dashboard_Admin.dto.admin.producto.ProductoAdminFormDTO;
import com.example.Bemole_Dashboard_Admin.dto.admin.producto.ProductoAdminResponseDTO;
import com.example.Bemole_Dashboard_Admin.exception.ApiClientException;
import com.example.Bemole_Dashboard_Admin.session.AdminSessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("admin/productos")
@AllArgsConstructor
public class AdminProductoViewController {
    private final AdminProductoApiClient productoApiClient;
    private final AdminCategoriaApiClient categoriaApiClient;
    private final AdminSessionService sessionService;

    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "10") int tamano, HttpSession session, Model model) {
        pagina = Math.max(pagina, 0);

        if (tamano < 1 || tamano > 50) {
            tamano = 10;
        }

        String token = sessionService.obtenerToken(session);

        PaginacionDTO<ProductoAdminResponseDTO> resultado = productoApiClient.listar(token, pagina, tamano);

        model.addAttribute(
                "paginaProductos",
                resultado
        );

        model.addAttribute("productos", resultado != null ? resultado.getContent() : java.util.List.of());

        return "admin/productos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarCrear(HttpSession session, Model model) {
        String token =
                sessionService.obtenerToken(session);

        model.addAttribute("producto", new ProductoAdminFormDTO());

        model.addAttribute("categorias", categoriaApiClient.listar(token));

        model.addAttribute("edicion", false);

        return "admin/productos/formulario";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("producto") ProductoAdminFormDTO producto, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String token = sessionService.obtenerToken(session);

        if (bindingResult.hasErrors()) {
            cargarFormulario(token, model, false, null);
            return "admin/productos/formulario";
        }

        try {
            productoApiClient.crear(token, producto);

            redirectAttributes.addFlashAttribute("mensaje", "El producto fue creado correctamente.");

            return "redirect:/admin/productos";

        } catch (ApiClientException exception) {
            cargarFormulario(token, model, false, null);

            model.addAttribute("error", exception.getMessage());

            return "admin/productos/formulario";
        }
    }

    @GetMapping("/{id}/editar")
    public String mostrarEditar(@PathVariable Long id,HttpSession session, Model model) {
        String token = sessionService.obtenerToken(session);

        ProductoAdminResponseDTO producto = productoApiClient.obtener(token, id);

        model.addAttribute("producto", ProductoAdminFormDTO.desde(producto));

        cargarFormulario(token, model, true, id);

        return "admin/productos/formulario";
    }

    @PutMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("producto") ProductoAdminFormDTO producto, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String token = sessionService.obtenerToken(session);

        if (bindingResult.hasErrors()) {
            cargarFormulario(token, model, true, id);

            return "admin/productos/formulario";
        }

        try {
            productoApiClient.actualizar(token, id, producto);

            redirectAttributes.addFlashAttribute("mensaje", "El producto fue actualizado correctamente.");

            return "redirect:/admin/productos";

        } catch (ApiClientException exception) {
            cargarFormulario(token, model, true, id);

            model.addAttribute("error", exception.getMessage());

            return "admin/productos/formulario";
        }
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            productoApiClient.eliminar(sessionService.obtenerToken(session), id);

            redirectAttributes.addFlashAttribute("mensaje", "El producto fue eliminado correctamente.");

        } catch (ApiClientException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }

        return "redirect:/admin/productos";
    }

    @PatchMapping("/{id}/activo")
    public String actualizarActivo(@PathVariable Long id, @RequestParam Boolean activo, @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "10") int tamano, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            productoApiClient.actualizarActivo(sessionService.obtenerToken(session), id, activo);

            redirectAttributes.addFlashAttribute("mensaje", Boolean.TRUE.equals(activo) ? "El producto fue activado correctamente." : "El producto fue desactivado correctamente.");

        } catch (ApiClientException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }

        redirectAttributes.addAttribute("pagina", Math.max(pagina, 0));

        redirectAttributes.addAttribute("tamano", tamano < 1 || tamano > 50 ? 10 : tamano);

        return "redirect:/admin/productos";
    }

    private void cargarFormulario(String token, Model model, boolean edicion, Long productoId) {
        model.addAttribute("categorias", categoriaApiClient.listar(token));

        model.addAttribute("edicion", edicion);

        model.addAttribute("productoId", productoId);
    }
}
