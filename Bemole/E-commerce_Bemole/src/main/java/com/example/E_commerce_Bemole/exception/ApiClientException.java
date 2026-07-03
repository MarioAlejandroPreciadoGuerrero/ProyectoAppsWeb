package com.example.E_commerce_Bemole.exception;

import com.example.E_commerce_Bemole.dto.error.ApiErrorDTO;

public class ApiClientException extends RuntimeException{
    private final ApiErrorDTO error;

    public ApiClientException(ApiErrorDTO error) {
        super(
                error != null ? error.getMensaje() : "Ocurrió un error al comunicarse con la API."
        );

        this.error = error;
    }

    public ApiErrorDTO getError() {
        return error;
    }
}
