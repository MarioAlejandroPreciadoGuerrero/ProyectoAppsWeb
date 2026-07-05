package com.example.Bemole_Dashboard_Admin.client;

import com.example.Bemole_Dashboard_Admin.dto.admin.AdminResumenDTO;
import com.example.Bemole_Dashboard_Admin.exception.AdminApiErrorHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@AllArgsConstructor
public class AdminResumenApiClient {
    private final RestClient restClient;
    private final AdminApiErrorHandler errorHandler;

    public AdminResumenDTO obtenerResumen(
            String token
    ) {
        return restClient.get()
                .uri("/api/admin/resumen")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .onStatus(status -> status.isError(), errorHandler::manejar)
                .body(AdminResumenDTO.class);
    }
}
