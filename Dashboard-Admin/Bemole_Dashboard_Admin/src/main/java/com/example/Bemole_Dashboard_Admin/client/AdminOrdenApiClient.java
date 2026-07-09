package com.example.Bemole_Dashboard_Admin.client;

import com.example.Bemole_Dashboard_Admin.dto.admin.orden.ActualizarEstadoOrdenFormDTO;
import com.example.Bemole_Dashboard_Admin.dto.admin.orden.OrdenDetalleResponseDTO;
import com.example.Bemole_Dashboard_Admin.dto.error.ApiErrorDTO;
import com.example.Bemole_Dashboard_Admin.enums.EstadoOrden;
import com.example.Bemole_Dashboard_Admin.dto.admin.orden.OrdenAdminResponseDTO;
import com.example.Bemole_Dashboard_Admin.dto.PaginacionDTO;
import com.example.Bemole_Dashboard_Admin.exception.AdminApiErrorHandler;
import com.example.Bemole_Dashboard_Admin.exception.ApiClientException;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Component
@AllArgsConstructor
public class AdminOrdenApiClient {
    private final RestClient restClient;
    private final AdminApiErrorHandler errorHandler;
    private final ObjectMapper objectMapper;

    public PaginacionDTO<OrdenAdminResponseDTO> listar(String token, EstadoOrden estado, int pagina, int tamano) {
        return restClient.get()
                .uri(uriBuilder -> {uriBuilder
                        .path("/api/admin/ordenes")
                        .queryParam("page", pagina)
                        .queryParam("size", tamano)
                        .queryParam("sort", "fecha,desc");

                    if (estado != null) {
                        uriBuilder.queryParam("estado", estado.name());
                    }

                    return uriBuilder.build();
                }).headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .onStatus(status -> status.isError(), errorHandler::manejar)
                .body(new ParameterizedTypeReference<PaginacionDTO<OrdenAdminResponseDTO>>() {});
    }

    public OrdenAdminResponseDTO actualizarEstado(String token, Long ordenId, EstadoOrden estado) {
        return restClient.patch()
                .uri("/api/admin/ordenes/{id}/estado", ordenId)
                .headers(headers -> headers.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ActualizarEstadoOrdenFormDTO(estado))
                .retrieve()
                .onStatus(status -> status.isError(), errorHandler::manejar)
                .body(OrdenAdminResponseDTO.class);
    }

    public OrdenDetalleResponseDTO obtenerDetalleOrden(
            String token,
            Long ordenId
    ) {
        return restClient.get()
                .uri(
                        "/api/admin/ordenes/{id}",
                        ordenId
                )
                .headers(headers ->
                        headers.setBearerAuth(token)
                )
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError()
                                || status.is5xxServerError(),
                        (request, response) -> {
                            ApiErrorDTO error =
                                    objectMapper.readValue(
                                            response.getBody(),
                                            ApiErrorDTO.class
                                    );

                            throw new ApiClientException(error);
                        }
                )
                .body(OrdenDetalleResponseDTO.class);
    }
}
