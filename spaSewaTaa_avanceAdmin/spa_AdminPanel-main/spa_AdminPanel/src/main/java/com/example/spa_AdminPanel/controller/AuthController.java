package com.example.spa_AdminPanel.controller;

import com.example.spa_AdminPanel.model.NombreRol;
import com.example.spa_AdminPanel.model.Rol;
import com.example.spa_AdminPanel.model.Usuario;
import com.example.spa_AdminPanel.service.RolService;
import com.example.spa_AdminPanel.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String inicio(HttpSession session) {
        String rol = (String) session.getAttribute("rol_usuario");
        if (rol == null) {
            return "redirect:/login";
        }
        switch (rol){
            case "ADMIN":
                return "redirect:/admin";
            case "TERAPEUTA":
                return "redirect:/terapeuta";
            case "CLIENTE":
                return "redirect:/cliente";
            default:
                return "redirect:/servicios";
        }
    }

    @GetMapping({"/login", "/auth/login"})
    public String mostrarInicioSesion() {
        return "auth/login";
    }

    @PostMapping({"/login", "/auth/login"})
    public String iniciarSesion(@RequestParam("username") String email,
                                @RequestParam("password") String contrasenia,
                                HttpSession session,
                                HttpServletRequest request,
                                HttpServletResponse response) {

        Usuario usuario = usuarioService.autenticarUsuario(email, contrasenia);

        if (usuario == null) {
            return "redirect:/login?error";
        }

        // 1. Guardar en tu sesión normal
        session.setAttribute("id_usuario", usuario.getId_usuario());
        session.setAttribute("rol_usuario", usuario.getRol().getNombre().name());

        // 2. Inyectar la sesión en Spring Security manualmente
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre().name());
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                usuario.getEmail(), null, Collections.singletonList(authority));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 3. Guardar el contexto explícitamente para que sobreviva al redirect (Spring Security 6)
        HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
        securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);

        return "redirect:/";
    }

    @GetMapping({"/registro", "/auth/registro"})
    public String mostrarRegistro() {
        return "auth/registro";
    }

    @PostMapping({"/registro", "/auth/registro"})
    public String registrar(@RequestParam String nombre,
                            @RequestParam String email,
                            @RequestParam("contrasenia") String contrasenia,
                            @RequestParam(required = false) String telefono,
                            @RequestParam String nombreRol) {
        try {
            String rolDefinir = nombreRol.trim().toLowerCase();
            NombreRol rol;
            switch(rolDefinir){
                case "admin" -> rol = NombreRol.ADMIN;
                case "terapeuta" -> rol = NombreRol.TERAPEUTA;
                case "cliente" -> rol = NombreRol.CLIENTE;
                default -> {
                    return "redirect:/registro";
                }
            }

            Rol rolUsuario = rolService.buscarPorNombre(rol);

            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setContrasenia(passwordEncoder.encode(contrasenia));
            usuario.setTelefono(telefono);
            usuario.setRol(rolUsuario);

            usuarioService.registrarUsuario(usuario);
            return "redirect:/login";
        } catch (Exception exception) {
            return "redirect:/registro?error";
        }
    }

    @PostMapping({"/logout", "/auth/logout"})
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }
}