package com.example.E_commerce_Bemole.client;

import com.example.E_commerce_Bemole.dto.error.ApiErrorDTO;
import com.example.E_commerce_Bemole.dto.pago.CrearPagoRequestDTO;
import com.example.E_commerce_Bemole.dto.pago.CrearPagoResponseDTO;
import com.example.E_commerce_Bemole.exception.ApiClientException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Component
@AllArgsConstructor
public class PagoApiClient {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public CrearPagoResponseDTO crearPago(String token, CrearPagoRequestDTO request){
        return restClient.post().uri("/api/pagos")
                .headers(headers ->
                        headers.setBearerAuth(token)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(
                        status-> status.is4xxClientError() || status.is5xxServerError(),
                        (httpRequest, response) -> {
                            ApiErrorDTO error = objectMapper.readValue(
                                    response.getBody(),
                                    ApiErrorDTO.class
                            );

                            throw new ApiClientException(error);
                        }
                ).body(CrearPagoResponseDTO.class);
    }
}
