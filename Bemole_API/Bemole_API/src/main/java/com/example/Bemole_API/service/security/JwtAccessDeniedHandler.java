package com.example.Bemole_API.service.security;

import com.example.Bemole_API.dto.error.ErrorResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler{
    private final ObjectMapper objectMapper;

    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.FORBIDDEN.value(),
                "ACCESO_DENEGADO",
                "No tienes permisos para acceder a este recurso",
                request.getRequestURI(),
                Map.of()
        );

        response.setStatus(
                HttpStatus.FORBIDDEN.value()
        );

        response.setContentType(
                MediaType.APPLICATION_JSON_VALUE
        );

        objectMapper.writeValue(
                response.getOutputStream(),
                error
        );
    }
}
