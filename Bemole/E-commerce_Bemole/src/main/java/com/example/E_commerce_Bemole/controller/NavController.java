package com.example.E_commerce_Bemole.controller;

import com.example.E_commerce_Bemole.client.CategoriaApiClient;
import com.example.E_commerce_Bemole.client.ProductoApiClient;
import com.example.E_commerce_Bemole.dto.PaginacionDTO;
import com.example.E_commerce_Bemole.dto.catalogo.ProductoResumenDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class NavController {

    private final CategoriaApiClient categoriaApiClient;
    private final ProductoApiClient productoApiClient;

    @GetMapping("/")
    public String inicio(Model model) {

        try {
            model.addAttribute("categorias", categoriaApiClient.listar());
        } catch (Exception exception) {
            exception.printStackTrace();

            model.addAttribute("categorias", java.util.List.of());

            model.addAttribute("errorCategorias", "No fue posible cargar las categorías.");
        }

        try {
            PaginacionDTO<ProductoResumenDTO> pagina =
                    productoApiClient.listar(
                            null,
                            null,
                            null,
                            null,
                            true,
                            null,
                            0,
                            4
                    );

            model.addAttribute("productosDestacados", pagina != null && pagina.getContenido() != null ? pagina.getContenido() : java.util.List.of());

        } catch (Exception exception) {
            exception.printStackTrace();

            model.addAttribute("productosDestacados", java.util.List.of());

            model.addAttribute("errorInicio", "No fue posible cargar los productos en este momento.");
        }

        return "home";
    }



    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }


    @GetMapping("/ordenes")
    public String ordenes(){
        return "ordenes";
    }
}
