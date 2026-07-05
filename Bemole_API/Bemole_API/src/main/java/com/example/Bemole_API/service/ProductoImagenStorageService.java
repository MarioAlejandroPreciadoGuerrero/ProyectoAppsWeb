package com.example.Bemole_API.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class ProductoImagenStorageService {
    private static final Set<String> TIPOS_PERMITIDOS = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final Path directorioProductos;

    public ProductoImagenStorageService(@Value("${bemole.upload.productos-directorio}") String directorioProductos) {
        this.directorioProductos = Path.of(directorioProductos).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void inicializar() {
        try {
            Files.createDirectories(directorioProductos);

        } catch (IOException exception) {
            throw new IllegalStateException("No fue posible crear el directorio de imágenes.", exception);
        }
    }

    public String guardar(MultipartFile archivo) {
        validarArchivo(archivo);

        String extension = obtenerExtension(archivo.getContentType());

        String nombreArchivo = UUID.randomUUID() + extension;

        Path destino = directorioProductos.resolve(nombreArchivo).normalize();

        if (!destino.startsWith(directorioProductos)) {
            throw new IllegalArgumentException("La ruta del archivo no es válida.");
        }

        try (InputStream inputStream = archivo.getInputStream()) {

            Files.copy(inputStream, destino, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException exception) {
            throw new IllegalStateException("No fue posible guardar la imagen.", exception);
        }

        return "/uploads/productos/" + nombreArchivo;
    }

    public void eliminar(String imagenUrl) {
        if (imagenUrl == null || imagenUrl.isBlank()) {
            return;
        }

        try {
            String nombreArchivo = Path.of(imagenUrl).getFileName().toString();

            Path archivo = directorioProductos.resolve(nombreArchivo).normalize();

            if (archivo.startsWith(directorioProductos)) {
                Files.deleteIfExists(archivo);
            }

        } catch (IOException exception) {
            /*
             * La eliminación de una imagen anterior no debe
             * impedir que el producto sea actualizado.
             */
        }
    }

    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("Debes seleccionar una imagen.");
        }

        String contentType = archivo.getContentType();

        if (contentType == null || !TIPOS_PERMITIDOS.contains(contentType)) {
            throw new IllegalArgumentException("Solo se permiten imágenes JPG, PNG o WEBP.");
        }
    }

    private String obtenerExtension(String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";

            default -> throw new IllegalArgumentException("El tipo de imagen no está permitido.");
        };
    }
}
