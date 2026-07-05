package com.example.Bemole_Dashboard_Admin.exception;

import com.example.Bemole_Dashboard_Admin.dto.error.ApiErrorDTO;
import com.example.Bemole_Dashboard_Admin.exception.ApiClientException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@AllArgsConstructor
public class AdminApiErrorHandler {
    private final ObjectMapper objectMapper;

    public void manejar(HttpRequest request, ClientHttpResponse response) throws IOException {

        ApiErrorDTO error;

        try {
            error = objectMapper.readValue(response.getBody(), ApiErrorDTO.class);

        } catch (Exception exception) {
            error = new ApiErrorDTO();
            error.setStatus(response.getStatusCode().value());
            error.setCodigo("ERROR_API");
            error.setMensaje("La API no pudo procesar la solicitud.");
            error.setRuta(request.getURI().getPath());
        }

        if (error.getStatus() == null) {
            error.setStatus(response.getStatusCode().value());
        }

        throw new ApiClientException(error);
    }
}
