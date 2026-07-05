package com.example.Bemole_Dashboard_Admin.controller;

import com.example.Bemole_Dashboard_Admin.client.AdminCategoriaApiClient;
import com.example.Bemole_Dashboard_Admin.dto.admin.categoria.CategoriaAdminFormDTO;
import com.example.Bemole_Dashboard_Admin.dto.admin.categoria.CategoriaAdminResponseDTO;
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

import java.util.List;

@Controller
@RequestMapping("/admin/categorias")
@AllArgsConstructor
public class AdminCategoriaViewController {
    private final AdminCategoriaApiClient categoriaApiClient;
    private final AdminSessionService sessionService;

    @GetMapping
    public String listar(HttpSession session, Model model) {
        String token = sessionService.obtenerToken(session);

        model.addAttribute("categorias", categoriaApiClient.listar(token));

        return "admin/categorias/lista";
    }

    @GetMapping("/nueva")
    public String mostrarCrear(Model model) {
        model.addAttribute("categoria", new CategoriaAdminFormDTO());

        model.addAttribute("edicion", false);

        return "admin/categorias/formulario";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("categoria") CategoriaAdminFormDTO categoria, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("edicion", false);
            return "admin/categorias/formulario";
        }

        try {
            categoriaApiClient.crear(sessionService.obtenerToken(session), categoria);

            redirectAttributes.addFlashAttribute("mensaje", "La categoría fue creada correctamente.");

            return "redirect:/admin/categorias";

        } catch (ApiClientException exception) {
            model.addAttribute("edicion", false);
            model.addAttribute("error", exception.getMessage());

            return "admin/categorias/formulario";
        }
    }

    @GetMapping("/{id}/editar")
    public String mostrarEditar(@PathVariable Long id, HttpSession session, Model model) {
        List<CategoriaAdminResponseDTO> categorias = categoriaApiClient.listar(sessionService.obtenerToken(session));

        CategoriaAdminResponseDTO encontrada = categorias.stream()
                .filter(categoria ->
                        categoria.getId().equals(id)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("La categoría solicitada no existe.")
                );

        model.addAttribute("categoria", CategoriaAdminFormDTO.desde(encontrada));

        model.addAttribute("edicion", true);
        model.addAttribute("categoriaId", id);

        return "admin/categorias/formulario";
    }

    @PutMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("categoria") CategoriaAdminFormDTO categoria, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("edicion", true);
            model.addAttribute("categoriaId", id);

            return "admin/categorias/formulario";
        }

        try {
            categoriaApiClient.actualizar(sessionService.obtenerToken(session), id, categoria);

            redirectAttributes.addFlashAttribute("mensaje", "La categoría fue actualizada correctamente.");

            return "redirect:/admin/categorias";

        } catch (ApiClientException exception) {
            model.addAttribute("edicion", true);
            model.addAttribute("categoriaId", id);
            model.addAttribute("error", exception.getMessage());

            return "admin/categorias/formulario";
        }
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            categoriaApiClient.eliminar(sessionService.obtenerToken(session), id);

            redirectAttributes.addFlashAttribute("mensaje", "La categoría fue eliminada correctamente.");

        } catch (ApiClientException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }

        return "redirect:/admin/categorias";
    }
}
