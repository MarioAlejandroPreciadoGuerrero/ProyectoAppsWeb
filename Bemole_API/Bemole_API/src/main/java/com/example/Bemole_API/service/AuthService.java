package com.example.Bemole_API.service;

import com.example.Bemole_API.dto.usuarios.AuthResponseDTO;
import com.example.Bemole_API.dto.usuarios.LoginDTO;
import com.example.Bemole_API.models.Usuario;
import com.example.Bemole_API.repositorys.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public AuthResponseDTO login(LoginDTO credenciales){
        Optional<Usuario> usuarioEmail= repository.findByEmail(credenciales.getEmail());

        if (usuarioEmail==null){
            throw new RuntimeException("Usuario no encontrado");
        }

        boolean passwordCorrecta = passwordEncoder.matches(credenciales.getPassword(), usuarioEmail.get().getPassword());

        if (!passwordCorrecta){
            throw new RuntimeException("Contraseña o email incorrectos");
        }

        return new AuthResponseDTO("Login exitoso",null);
    }
}
