package com.example.Bemole_API.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class ImagenUrlService {
    private final String urlPublica;

    public ImagenUrlService(@Value("${bemole.upload.url-publica}") String urlPublica) {
        this.urlPublica = normalizarUrlBase(urlPublica);
    }

    public String construirUrl(String rutaImagen) {
        if (rutaImagen == null || rutaImagen.isBlank()) {
            return null;
        }

        String ruta = rutaImagen.trim();

        if (ruta.startsWith("http://") || ruta.startsWith("https://")) {
            return ruta;
        }

        if (!ruta.startsWith("/")) {
            ruta = "/" + ruta;
        }

        return urlPublica + ruta;
    }

    private String normalizarUrlBase(String url) {
        if (url == null || url.isBlank()) {
            return "";
        }

        String resultado = url.trim();

        while (resultado.endsWith("/")) {
            resultado = resultado.substring(0, resultado.length() - 1);
        }

        return resultado;
    }
}
