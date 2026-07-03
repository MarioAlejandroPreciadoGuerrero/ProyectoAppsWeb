package com.example.E_commerce_Bemole.client;

import com.example.E_commerce_Bemole.dto.error.ApiErrorDTO;
import com.example.E_commerce_Bemole.dto.ordenes.CrearOrdenFormDTO;
import com.example.E_commerce_Bemole.dto.ordenes.response.OrdenCreadaResponseDTO;
import com.example.E_commerce_Bemole.exception.ApiClientException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Component
@AllArgsConstructor
public class OrdenApiClient {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public OrdenCreadaResponseDTO crearOrden(String token, CrearOrdenFormDTO request) {
        return restClient.post().uri("/api/ordenes")
                .headers(headers ->
                        headers.setBearerAuth(token)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        (httpRequest, response) -> {
                            ApiErrorDTO error = objectMapper.readValue(
                                            response.getBody(),
                                            ApiErrorDTO.class
                                    );

                            throw new ApiClientException(error);
                        }
                ).body(OrdenCreadaResponseDTO.class);
    }
}
