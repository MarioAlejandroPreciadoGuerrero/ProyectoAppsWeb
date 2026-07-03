package com.example.E_commerce_Bemole.client;

import com.example.E_commerce_Bemole.dto.catalogo.CategoriaResumenDTO;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@AllArgsConstructor
public class CategoriaApiClient {

    private final RestClient restClient;

    public List<CategoriaResumenDTO> listar() {
        List<CategoriaResumenDTO> categorias =
                restClient.get()
                        .uri("/api/categoria")
                        .retrieve()
                        .body(
                                new ParameterizedTypeReference<List<CategoriaResumenDTO>>() {

                                }
                        );

        return categorias != null ? categorias : List.of();
    }
}
