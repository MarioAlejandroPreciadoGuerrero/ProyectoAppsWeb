package com.example.Bemole_Dashboard_Admin.client;

import com.example.Bemole_Dashboard_Admin.dto.admin.orden.ActualizarEstadoOrdenFormDTO;
import com.example.Bemole_Dashboard_Admin.dto.admin.orden.OrdenAdminResponseDTO;
import com.example.Bemole_Dashboard_Admin.dto.admin.producto.ActualizarActivoProductoDTO;
import com.example.Bemole_Dashboard_Admin.dto.admin.producto.ProductoAdminFormDTO;
import com.example.Bemole_Dashboard_Admin.dto.admin.producto.ProductoAdminResponseDTO;
import com.example.Bemole_Dashboard_Admin.dto.PaginacionDTO;
import com.example.Bemole_Dashboard_Admin.enums.EstadoOrden;
import com.example.Bemole_Dashboard_Admin.exception.AdminApiErrorHandler;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
public class AdminProductoApiClient {
    private final RestClient restClient;
    private final AdminApiErrorHandler errorHandler;

    public PaginacionDTO<ProductoAdminResponseDTO> listar(String token, int pagina, int tamano) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/admin/productos")
                        .queryParam("page", pagina)
                        .queryParam("size", tamano)
                        .queryParam("sort", "nombre,asc")
                        .build()
                ).headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .onStatus(status -> status.isError(), errorHandler::manejar)
                .body(new ParameterizedTypeReference<PaginacionDTO<ProductoAdminResponseDTO>>() {}
                );
    }

    public ProductoAdminResponseDTO obtener(String token, Long id) {
        return restClient.get()
                .uri("/api/admin/productos/{id}", id)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .onStatus(status -> status.isError(), errorHandler::manejar)
                .body(ProductoAdminResponseDTO.class);
    }

    public ProductoAdminResponseDTO crear(String token, ProductoAdminFormDTO formulario) {
        return restClient.post()
                .uri("/api/admin/productos")
                .headers(headers -> headers.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .body(formulario)
                .retrieve()
                .onStatus(status -> status.isError(), errorHandler::manejar)
                .body(ProductoAdminResponseDTO.class);
    }

    public ProductoAdminResponseDTO actualizar(String token, Long id, ProductoAdminFormDTO formulario) {
        return restClient.put()
                .uri("/api/admin/productos/{id}", id)
                .headers(headers -> headers.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .body(formulario)
                .retrieve()
                .onStatus(status -> status.isError(), errorHandler::manejar)
                .body(ProductoAdminResponseDTO.class);
    }

    public void eliminar(String token, Long id) {
        restClient.delete()
                .uri("/api/admin/productos/{id}", id)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .onStatus(status -> status.isError(), errorHandler::manejar)
                .toBodilessEntity();
    }

    public ProductoAdminResponseDTO actualizarActivo(String token, Long id, Boolean activo) {
        ActualizarActivoProductoDTO request = new ActualizarActivoProductoDTO(activo);

        return restClient.patch()
                .uri("/api/admin/productos/{id}/activo", id)
                .headers(headers -> headers.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(status -> status.isError(), errorHandler::manejar)
                .body(ProductoAdminResponseDTO.class);
    }

    public ProductoAdminResponseDTO actualizarImagen(String token, Long productoId, MultipartFile imagen) {
        if (imagen == null || imagen.isEmpty()) {
            throw new IllegalArgumentException("Debes seleccionar una imagen.");
        }

        HttpHeaders headersArchivo = new HttpHeaders();

        if (imagen.getContentType() != null) {
            headersArchivo.setContentType(MediaType.parseMediaType(imagen.getContentType()));
        }

        HttpEntity<?> parteArchivo = new HttpEntity<>(
                imagen.getResource(),
                headersArchivo
        );

        MultiValueMap<String, Object> partes = new LinkedMultiValueMap<>();

        partes.add("imagen", parteArchivo);

        return restClient.put()
                .uri("/api/admin/productos/{id}/imagen", productoId)
                .headers(headers -> headers.setBearerAuth(token))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(partes)
                .retrieve()
                .onStatus(status -> status.isError(), errorHandler::manejar)
                .body(ProductoAdminResponseDTO.class);
    }
}
