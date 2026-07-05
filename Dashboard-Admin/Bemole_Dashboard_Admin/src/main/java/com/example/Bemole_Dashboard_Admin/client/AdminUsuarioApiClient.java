package com.example.Bemole_Dashboard_Admin.client;

import com.example.Bemole_Dashboard_Admin.dto.admin.usuario.UsuarioAdminResponseDTO;
import com.example.Bemole_Dashboard_Admin.dto.PaginacionDTO;
import com.example.Bemole_Dashboard_Admin.exception.AdminApiErrorHandler;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@AllArgsConstructor
public class AdminUsuarioApiClient {
    private final RestClient restClient;
    private final AdminApiErrorHandler errorHandler;

    public PaginacionDTO<UsuarioAdminResponseDTO> listar(String token, int pagina, int tamano) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/admin/usuarios")
                        .queryParam("page", pagina)
                        .queryParam("size", tamano)
                        .queryParam("sort", "fechaRegistro,desc")
                        .build()
                )
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .onStatus(status -> status.isError(), errorHandler::manejar)
                .body(new ParameterizedTypeReference<PaginacionDTO<UsuarioAdminResponseDTO>>() {});
    }
}
