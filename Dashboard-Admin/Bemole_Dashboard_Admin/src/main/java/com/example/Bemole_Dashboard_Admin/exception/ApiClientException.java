package com.example.Bemole_Dashboard_Admin.exception;

import com.example.Bemole_Dashboard_Admin.dto.error.ApiErrorDTO;

public class ApiClientException extends RuntimeException{
    private final ApiErrorDTO error;

    public ApiClientException(ApiErrorDTO error) {
        super(
                error != null && error.getMensaje() != null ? error.getMensaje() : "La API devolvió un error."
        );

        this.error = error;
    }

    public ApiErrorDTO getError() {
        return error;
    }

    public int getStatus() {
        return error != null && error.getStatus() != null ? error.getStatus() : 500;
    }
}
