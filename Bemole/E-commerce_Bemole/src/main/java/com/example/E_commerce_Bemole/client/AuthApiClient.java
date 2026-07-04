package com.example.E_commerce_Bemole.client;

import com.example.E_commerce_Bemole.dto.usuarios.LoginFormDTO;
import com.example.E_commerce_Bemole.dto.usuarios.RegistroFormDTO;
import com.example.E_commerce_Bemole.dto.usuarios.SesionResponseDTO;
import com.example.E_commerce_Bemole.dto.usuarios.UsuarioRegistradoDTO;
import com.example.E_commerce_Bemole.dto.error.ApiErrorDTO;
import com.example.E_commerce_Bemole.exception.ApiClientException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Component
@Data
@AllArgsConstructor
public class AuthApiClient {
    @Autowired
    private final RestClient restClient;

    @Autowired
    private final ObjectMapper objectMapper;

    public SesionResponseDTO iniciarSesion(LoginFormDTO request) {
        return restClient.post()
                .uri("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (httpRequest, response) -> {
                    ApiErrorDTO error = objectMapper.readValue(
                            response.getBody(),
                            ApiErrorDTO.class
                    );

                    throw new ApiClientException(error);
                    }
                )
                .body(SesionResponseDTO.class);
    }

    public UsuarioRegistradoDTO registrar(RegistroFormDTO request) {
        return restClient.post()
                .uri("api/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError()
                                || status.is5xxServerError(),
                        (httpRequest, response) -> {
                            ApiErrorDTO error = objectMapper.readValue(
                                    response.getBody(),
                                    ApiErrorDTO.class
                            );

                            throw new ApiClientException(error);
                        }
                )
                .body(UsuarioRegistradoDTO.class);
    }
}
