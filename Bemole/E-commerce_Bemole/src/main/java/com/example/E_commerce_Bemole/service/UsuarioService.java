package com.example.E_commerce_Bemole.service;

import com.example.E_commerce_Bemole.dto.usuarios.UsuarioRegistroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UsuarioService {
    @Autowired
    private RestTemplate restTemplate;

    private final String API_URL = "http://localhost:8081/api/usuario";

    public UsuarioRegistroDTO registrar(UsuarioRegistroDTO dto) {
        return restTemplate.postForObject(
                API_URL,
                dto,
                UsuarioRegistroDTO.class
        );
    }
}
