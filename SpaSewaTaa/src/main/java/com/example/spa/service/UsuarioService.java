package com.example.spa.service;

import com.example.spa.model.Cita;
import com.example.spa.model.Usuario;
import com.example.spa.repository.CitaRepository;
import com.example.spa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession session;


    public Usuario registrarUsuario(Usuario user) {
        user.setContrasenia(passwordEncoder.encode(user.getContrasenia()));
        return usuarioRepository.save(user);
    }

    public Usuario autenticarUsuario(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findUsuarioByEmail(email);
        if (usuarioOpt.isPresent()) {
            Usuario user = usuarioOpt.get();
            if (passwordEncoder.matches(password, user.getContrasenia())) {
                return user;
            }
        }
        return null;
    }

    public Usuario obtenerUsuarioLogueado() {
        Long usuarioId = (Long) session.getAttribute("id_usuario");
        if (usuarioId != null) {
            return usuarioRepository.findById(usuarioId).orElse(null);
        }
        return null;
    }

}