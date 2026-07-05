package com.example.Bemole_API.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class ArchivosWebConfig implements WebMvcConfigurer {
    private final String directorioProductos;

    public ArchivosWebConfig(@Value("${bemole.upload.productos-directorio}") String directorioProductos) {
        this.directorioProductos = directorioProductos;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String ubicacion = Path.of(directorioProductos).toAbsolutePath().normalize().toUri().toString();

        registry.addResourceHandler("/uploads/productos/**").addResourceLocations(ubicacion);
    }
}
