package com.example.E_commerce_Bemole.controller;

import com.example.E_commerce_Bemole.client.ProductoApiClient;
import com.example.E_commerce_Bemole.dto.catalogo.ProductoDetalleDTO;
import com.example.E_commerce_Bemole.exception.ApiClientException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class ProductoViewController {
    private final ProductoApiClient productoApiClient;

    @GetMapping("/producto/{id}")
    public String mostrarDetalle(@PathVariable Long id, Model model) {
        try {
            ProductoDetalleDTO producto = productoApiClient.obtenerDetalle(id);

            model.addAttribute("producto", producto);

            return "producto";

        } catch (ApiClientException exception) {
            if (exception.getError() != null && exception.getError().getStatus() == 404) {
                return "redirect:/catalogo?productoNoEncontrado";
            }

            model.addAttribute("errorProducto", exception.getMessage());

            return "producto";

        } catch (Exception exception) {
            model.addAttribute("errorProducto", "No fue posible cargar el producto.");
            return "producto";
        }
    }
}
