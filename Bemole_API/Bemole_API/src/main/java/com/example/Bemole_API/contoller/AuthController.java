package com.example.Bemole_API.contoller;

import com.example.Bemole_API.dto.usuarios.AuthResponseDTO;
import com.example.Bemole_API.dto.usuarios.LoginDTO;
import com.example.Bemole_API.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO credenciales){
        return ResponseEntity.ok(service.login(credenciales));
    }
}
