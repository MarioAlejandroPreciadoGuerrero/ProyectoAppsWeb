package com.example.Bemole_API.exception;

import com.example.Bemole_API.dto.error.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /*
     * Errores producidos por @Valid:
     * @NotBlank, @Email, @Size, @Min, etc.
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> manejarValidaciones(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> campos = new LinkedHashMap<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            campos.putIfAbsent(
                    error.getField(),
                    error.getDefaultMessage()
            );
        }

        ErrorResponseDTO respuesta = crearRespuesta(
                HttpStatus.BAD_REQUEST,
                "DATOS_INVALIDOS",
                "La solicitud contiene datos inválidos",
                request.getRequestURI(),
                campos
        );

        return ResponseEntity.badRequest().body(respuesta);
    }

    /*
     * JSON mal formado, enum inválido, tipo de dato incorrecto, etc.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> manejarJsonInvalido(HttpMessageNotReadableException exception, HttpServletRequest request) {
        ErrorResponseDTO respuesta = crearRespuesta(
                HttpStatus.BAD_REQUEST,
                "JSON_INVALIDO",
                "El cuerpo de la solicitud no tiene un formato válido",
                request.getRequestURI(),
                Map.of()
        );

        return ResponseEntity.badRequest().body(respuesta);
    }

    /*
     * Recurso inexistente.
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> manejarRecursoNoEncontrado(RecursoNoEncontradoException exception, HttpServletRequest request) {
        ErrorResponseDTO respuesta = crearRespuesta(
                HttpStatus.NOT_FOUND,
                "RECURSO_NO_ENCONTRADO",
                exception.getMessage(),
                request.getRequestURI(),
                Map.of()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    /*
     * Reglas del negocio: stock, correo duplicado, carrito vacío, etc.
     */
    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErrorResponseDTO> manejarReglaNegocio(NegocioException exception, HttpServletRequest request) {
        ErrorResponseDTO respuesta = crearRespuesta(
                HttpStatus.CONFLICT,
                "REGLA_NEGOCIO",
                exception.getMessage(),
                request.getRequestURI(),
                Map.of()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    /*
     * Restricciones de la base de datos:
     * correo UNIQUE, llaves foráneas, valores duplicados, etc.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> manejarIntegridadDatos(DataIntegrityViolationException exception, HttpServletRequest request) {
        ErrorResponseDTO respuesta = crearRespuesta(
                HttpStatus.CONFLICT,
                "CONFLICTO_DATOS",
                "La operación entra en conflicto con datos existentes",
                request.getRequestURI(),
                Map.of()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    /*
     * Credenciales incorrectas.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> manejarCredencialesIncorrectas(BadCredentialsException exception, HttpServletRequest request) {
        ErrorResponseDTO respuesta = crearRespuesta(
                HttpStatus.UNAUTHORIZED,
                "CREDENCIALES_INVALIDAS",
                "El correo o la contraseña son incorrectos",
                request.getRequestURI(),
                Map.of()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
    }

    /*
     * Usuario autenticado, pero sin permisos.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> manejarAccesoDenegado(AccessDeniedException exception, HttpServletRequest request) {
        ErrorResponseDTO respuesta = crearRespuesta(
                HttpStatus.FORBIDDEN,
                "ACCESO_DENEGADO",
                "No tienes permisos para realizar esta operación",
                request.getRequestURI(),
                Map.of()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
    }

    /*
     * Última barrera para errores inesperados.
     * No se devuelve exception.getMessage() porque podría revelar
     * información interna de la aplicación.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> manejarErrorGeneral(Exception exception, HttpServletRequest request) {
        // El stacktrace queda solamente en la consola del servidor.
        exception.printStackTrace();

        ErrorResponseDTO respuesta = crearRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "ERROR_INTERNO",
                "Ocurrió un error inesperado",
                request.getRequestURI(),
                Map.of()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> manejarArgumentoInvalido(IllegalArgumentException exception, HttpServletRequest request) {
        ErrorResponseDTO respuesta = crearRespuesta(
                HttpStatus.BAD_REQUEST,
                "DATOS_INVALIDOS",
                exception.getMessage(),
                request.getRequestURI(),
                Map.of()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    private ErrorResponseDTO crearRespuesta(HttpStatus estado, String codigo, String mensaje, String ruta, Map<String, String> campos) {
        return new ErrorResponseDTO(
                Instant.now(),
                estado.value(),
                codigo,
                mensaje,
                ruta,
                campos
        );
    }

}
