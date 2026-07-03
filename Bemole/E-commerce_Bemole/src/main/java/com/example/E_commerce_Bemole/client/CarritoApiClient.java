package com.example.E_commerce_Bemole.client;

import com.example.E_commerce_Bemole.dto.carrito.ActualizarCantidadItemFormDTO;
import com.example.E_commerce_Bemole.dto.carrito.AgregarItemCarritoFormDTO;
import com.example.E_commerce_Bemole.dto.carrito.CarritoResponseDTO;
import com.example.E_commerce_Bemole.dto.error.ApiErrorDTO;
import com.example.E_commerce_Bemole.exception.ApiClientException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Component
@AllArgsConstructor
public class CarritoApiClient {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public CarritoResponseDTO obtenerCarrito(String token) {
        return restClient.get()
                .uri("/api/carrito")
                .headers(headers ->
                        headers.setBearerAuth(token)
                )
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        (request, response) -> {
                            ApiErrorDTO error = objectMapper.readValue(
                                            response.getBody(),
                                            ApiErrorDTO.class
                                    );

                            throw new ApiClientException(error);
                        }
                )
                .body(CarritoResponseDTO.class);
    }

    public CarritoResponseDTO agregarItem(String token, AgregarItemCarritoFormDTO request) {
        return restClient.post()
                .uri("/api/carrito/items")
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
                )
                .body(CarritoResponseDTO.class);
    }

    public CarritoResponseDTO actualizarCantidad(String token, Long itemId, ActualizarCantidadItemFormDTO request) {
        return restClient.patch()
                .uri("/api/carrito/items/{itemId}", itemId)
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
                )
                .body(CarritoResponseDTO.class);
    }

    public CarritoResponseDTO eliminarItem(String token, Long itemId) {
        return restClient.delete()
                .uri("/api/carrito/items/{itemId}", itemId).headers(headers ->
                        headers.setBearerAuth(token)
                )
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
                )
                .body(CarritoResponseDTO.class);
    }


}
