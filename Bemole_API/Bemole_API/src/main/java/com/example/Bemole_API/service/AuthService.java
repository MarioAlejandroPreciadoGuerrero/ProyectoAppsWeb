package com.example.Bemole_API.service;

import com.example.Bemole_API.dto.usuarios.SesionResponseDTO;
import com.example.Bemole_API.dto.usuarios.InicioSesionRequestDTO;
import com.example.Bemole_API.dto.usuarios.UsuarioResumenDTO;
import com.example.Bemole_API.exception.NegocioException;
import com.example.Bemole_API.exception.RecursoNoEncontradoException;
import com.example.Bemole_API.models.Usuario;
import com.example.Bemole_API.repositorys.UsuarioRepository;
import com.example.Bemole_API.service.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public SesionResponseDTO iniciarSesion(
            InicioSesionRequestDTO request
    ) {
        String emailNormalizado = request
                .getEmail()
                .trim()
                .toLowerCase();

        Usuario usuario = repository.findByEmail(emailNormalizado).orElseThrow(() ->
                        new BadCredentialsException(
                                "Credenciales incorrectas"
                        )
                );

        boolean passwordCorrecta = passwordEncoder.matches(
                request.getPassword(),
                usuario.getPassword()
        );

        if (!passwordCorrecta) {
            throw new BadCredentialsException(
                    "Credenciales incorrectas"
            );
        }

        String token = jwtService.generarToken(usuario);

        UsuarioResumenDTO usuarioDTO =
                new UsuarioResumenDTO(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getEmail(),
                        usuario.getRol()
                );

        return new SesionResponseDTO(
                token,
                "Bearer",
                jwtService.obtenerExpiracionEnSegundos(),
                usuarioDTO
        );
    }
}
