package com.example.Bemole_API.config;

import com.example.Bemole_API.service.security.JwtAccessDeniedHandler;
import com.example.Bemole_API.service.security.JwtAuthenticationEntryPoint;
import com.example.Bemole_API.service.security.JwtAuthenticationFilter;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter, JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAccessDeniedHandler accessDeniedHandler) {
        this.jwtFilter = jwtFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http.csrf(csrf -> csrf.disable()).sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .exceptionHandling(exceptions ->
                        exceptions
                                .authenticationEntryPoint(
                                        authenticationEntryPoint
                                )
                                .accessDeniedHandler(
                                        accessDeniedHandler
                                )
                )
                .authorizeHttpRequests(auth -> auth

                        // Registro
                        .requestMatchers(
                                "/api/usuarios"
                        ).permitAll()

                        // Inicio de sesión
                        .requestMatchers(
                                "/api/auth"
                        ).permitAll()

                        // Catálogo público
                        .requestMatchers(
                                "/api/productos/**",
                                "/api/categorias/**"
                        ).permitAll()

                        // Futuras rutas administrativas
                        .requestMatchers(
                                "/api/admin/**"
                        ).hasRole("ADMINISTRADOR")

                        // Todo lo demás requiere JWT
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
