package com.example.E_commerce_Bemole.controller;

import com.example.E_commerce_Bemole.client.CategoriaApiClient;
import com.example.E_commerce_Bemole.client.ProductoApiClient;
import com.example.E_commerce_Bemole.dto.PaginacionDTO;
import com.example.E_commerce_Bemole.dto.catalogo.FiltroCatalogoDTO;
import com.example.E_commerce_Bemole.dto.catalogo.ProductoResumenDTO;
import com.example.E_commerce_Bemole.exception.ApiClientException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@AllArgsConstructor
public class CatalogoController {
    private final ProductoApiClient productoApiClient;
    private final CategoriaApiClient categoriaApiClient;

    @GetMapping("/catalogo")
    public String mostrarCatalogo(
            @ModelAttribute("filtro")
            FiltroCatalogoDTO filtro,
            Model model
    ) {
        corregirPaginacion(filtro);

        try {
            PaginacionDTO<ProductoResumenDTO> resultado =
                    productoApiClient.listar(
                            filtro.getQ(),
                            filtro.getCategoriaId(),
                            filtro.getPrecioMin(),
                            filtro.getPrecioMax(),
                            filtro.isSoloConStock(),
                            filtro.getOrden(),
                            filtro.getPagina(),
                            filtro.getTamano()
                    );

            model.addAttribute(
                    "paginaProductos",
                    resultado
            );

            model.addAttribute(
                    "productos",
                    resultado != null
                            ? resultado.getContenido()
                            : java.util.List.of()
            );

            model.addAttribute(
                    "categorias",
                    categoriaApiClient.listar()
            );

        } catch (ApiClientException exception) {
            model.addAttribute(
                    "productos",
                    java.util.List.of()
            );

            model.addAttribute(
                    "categorias",
                    categoriaApiClient.listar()
            );

            model.addAttribute(
                    "errorCatalogo",
                    exception.getMessage()
            );

        } catch (Exception exception) {
            model.addAttribute(
                    "productos",
                    java.util.List.of()
            );

            model.addAttribute(
                    "categorias",
                    java.util.List.of()
            );

            model.addAttribute(
                    "errorCatalogo",
                    "No fue posible cargar el catálogo."
            );
        }

        return "catalogo";
    }

    private void corregirPaginacion(FiltroCatalogoDTO filtro) {
        if (filtro.getPagina() < 0) {
            filtro.setPagina(0);
        }

        if (filtro.getTamano() < 1 || filtro.getTamano() > 50) {
            filtro.setTamano(12);
        }
    }
}
