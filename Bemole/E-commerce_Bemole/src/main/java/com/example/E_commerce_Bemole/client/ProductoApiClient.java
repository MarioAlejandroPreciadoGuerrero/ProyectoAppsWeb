package com.example.E_commerce_Bemole.client;

import com.example.E_commerce_Bemole.enums.OrdenProducto;
import com.example.E_commerce_Bemole.dto.catalogo.ProductoDetalleDTO;
import com.example.E_commerce_Bemole.dto.catalogo.ProductoResumenDTO;
import com.example.E_commerce_Bemole.dto.PaginacionDTO;
import com.example.E_commerce_Bemole.dto.error.ApiErrorDTO;
import com.example.E_commerce_Bemole.exception.ApiClientException;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.net.URI;

@Component
@AllArgsConstructor
public class ProductoApiClient {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public PaginacionDTO<ProductoResumenDTO> listar(String q, Long categoriaId, BigDecimal precioMin, BigDecimal precioMax, Boolean soloConStock, OrdenProducto orden, int pagina, int tamano) {
        return restClient.get().uri(uriBuilder -> construirUriCatalogo(
                        uriBuilder, q, categoriaId, precioMin, precioMax, soloConStock, orden, pagina, tamano))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (request, response) -> {
                            ApiErrorDTO error =
                                    objectMapper.readValue(
                                            response.getBody(),
                                            ApiErrorDTO.class
                                    );

                            throw new ApiClientException(error);
                        }
                )
                .body(
                        new ParameterizedTypeReference<
                                PaginacionDTO<ProductoResumenDTO>
                                >() {
                        }
                );
    }

    public ProductoDetalleDTO obtenerDetalle(Long productoId) {
        return restClient.get()
                .uri("/api/producto/{id}", productoId).retrieve().onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        (request, response) -> {
                            ApiErrorDTO error =
                                    objectMapper.readValue(
                                            response.getBody(),
                                            ApiErrorDTO.class
                                    );

                            throw new ApiClientException(error);
                        }
                )
                .body(ProductoDetalleDTO.class);
    }

    private URI construirUriCatalogo(UriBuilder uriBuilder, String q, Long categoriaId, BigDecimal precioMin, BigDecimal precioMax, Boolean soloConStock, OrdenProducto orden, int pagina, int tamano) {
        uriBuilder.path("/api/producto");

        if (q != null && !q.isBlank()) {
            uriBuilder.queryParam("q", q.trim());
        }

        if (categoriaId != null) {
            uriBuilder.queryParam("categoriaId", categoriaId);
        }

        if (precioMin != null) {
            uriBuilder.queryParam("precioMin", precioMin);
        }

        if (precioMax != null) {
            uriBuilder.queryParam("precioMax", precioMax);
        }

        if (Boolean.TRUE.equals(soloConStock)) {
            uriBuilder.queryParam("soloConStock", true);
        }

        if (orden != null) {
            uriBuilder.queryParam("orden", orden.name());
        }

        uriBuilder.queryParam("pagina", pagina);

        uriBuilder.queryParam("tamano", tamano);

        return uriBuilder.build();
    }
}
