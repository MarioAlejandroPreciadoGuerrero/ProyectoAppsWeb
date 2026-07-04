package com.example.Bemole_Dashboard_Admin.client;

import lombok.AllArgsConstructor;
import com.example.Bemole_Dashboard_Admin.dto.auth.AdminLoginFormDTO;
import com.example.Bemole_Dashboard_Admin.dto.auth.response.SesionResponseDTO;
import com.example.Bemole_Dashboard_Admin.dto.error.ApiErrorDTO;
import com.example.Bemole_Dashboard_Admin.exception.ApiClientException;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
@AllArgsConstructor
public class AdminAuthApiClient {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public SesionResponseDTO iniciarSesion(
            AdminLoginFormDTO formulario
    ) {
        return restClient.post()
                .uri("/api/auth").contentType(MediaType.APPLICATION_JSON).body(formulario).retrieve().onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),

                        (request, response) -> {
                            ApiErrorDTO error;

                            try {
                                error = objectMapper.readValue(
                                        response.getBody(),
                                        ApiErrorDTO.class
                                );

                            } catch (Exception exception) {
                                error = new ApiErrorDTO();
                                error.setStatus(response.getStatusCode().value());
                                error.setCodigo("ERROR_API");
                                error.setMensaje("La API no pudo procesar la solicitud.");
                            }

                            throw new ApiClientException(error);
                        }
                ).body(SesionResponseDTO.class);
    }
}
