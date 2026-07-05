package com.example.Bemole_Dashboard_Admin.client;

import com.example.Bemole_Dashboard_Admin.dto.admin.categoria.CategoriaAdminFormDTO;
import com.example.Bemole_Dashboard_Admin.dto.admin.categoria.CategoriaAdminResponseDTO;
import com.example.Bemole_Dashboard_Admin.exception.AdminApiErrorHandler;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@AllArgsConstructor
public class AdminCategoriaApiClient {
    private final RestClient restClient;
    private final AdminApiErrorHandler errorHandler;

    public List<CategoriaAdminResponseDTO> listar(String token) {
        List<CategoriaAdminResponseDTO> categorias =
                restClient.get()
                        .uri("/api/admin/categorias")
                        .headers(headers -> headers.setBearerAuth(token))
                        .retrieve()
                        .onStatus(status -> status.isError(), errorHandler::manejar)
                        .body(new ParameterizedTypeReference<List<CategoriaAdminResponseDTO>>() {});

        return categorias != null ? categorias : List.of();
    }

    public CategoriaAdminResponseDTO crear(String token, CategoriaAdminFormDTO formulario) {
        return restClient.post()
                .uri("/api/admin/categorias")
                .headers(headers -> headers.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .body(formulario)
                .retrieve()
                .onStatus(status -> status.isError(), errorHandler::manejar)
                .body(CategoriaAdminResponseDTO.class);
    }

    public CategoriaAdminResponseDTO actualizar(String token, Long id, CategoriaAdminFormDTO formulario) {
        return restClient.put()
                .uri("/api/admin/categorias/{id}", id)
                .headers(headers -> headers.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .body(formulario)
                .retrieve()
                .onStatus(status -> status.isError(), errorHandler::manejar)
                .body(CategoriaAdminResponseDTO.class);
    }

    public void eliminar(String token, Long id) {
        restClient.delete()
                .uri("/api/admin/categorias/{id}", id)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .onStatus(status -> status.isError(), errorHandler::manejar)
                .toBodilessEntity();
    }
}
