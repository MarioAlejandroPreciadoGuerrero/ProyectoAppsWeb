package com.example.Bemole_API.service.security;

import com.example.Bemole_API.models.Usuario;
import com.example.Bemole_API.repositorys.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UsuarioRepository repository;

    public JwtAuthenticationFilter(JwtService jwtService, UsuarioRepository repository) {
        this.jwtService = jwtService;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);

        try {
            String email = jwtService.obtenerEmail(token);

            boolean noAutenticado =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null;

            if (email != null && noAutenticado) {
                Usuario usuario = repository
                        .findByEmail(email)
                        .orElse(null);

                if (usuario != null
                        && jwtService.esTokenValido(
                        token,
                        usuario
                )) {

                    String autoridad =
                            "ROLE_" + usuario.getRol().name();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    usuario,
                                    null,
                                    List.of(
                                            new SimpleGrantedAuthority(
                                                    autoridad
                                            )
                                    )
                            );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (JwtException | IllegalArgumentException exception) {
            /*
             * El token es inválido, está expirado o tiene
             * una firma incorrecta.
             *
             * No autenticamos la petición. Spring Security
             * responderá 401 si intenta entrar a una ruta
             * protegida.
             */
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
