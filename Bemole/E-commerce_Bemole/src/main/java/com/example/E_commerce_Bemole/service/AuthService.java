package com.example.E_commerce_Bemole.service;

import com.example.E_commerce_Bemole.dto.AuthResponseDTO;
import com.example.E_commerce_Bemole.dto.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    @Autowired
    private RestTemplate restTemplate;

    private final String API_URL = "http://localhost:8081/api/auth/login";

    public AuthResponseDTO login(LoginDTO credenciales){
        return restTemplate.postForObject(API_URL,credenciales, AuthResponseDTO.class);
    }
}
