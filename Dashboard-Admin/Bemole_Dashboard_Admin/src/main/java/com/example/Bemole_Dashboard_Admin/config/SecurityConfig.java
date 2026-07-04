package com.example.Bemole_Dashboard_Admin.config;

import com.example.Bemole_Dashboard_Admin.security.AdminSessionAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private final AdminSessionAuthenticationFilter adminFilter;

    public SecurityConfig(
            AdminSessionAuthenticationFilter adminFilter
    ) {
        this.adminFilter = adminFilter;
    }

    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/acceso-denegado",
                                "/error",
                                "/styles/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico"
                        ).permitAll()

                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        .anyRequest()
                        .authenticated()
                )

                .formLogin(
                        AbstractHttpConfigurer::disable
                )

                .httpBasic(
                        AbstractHttpConfigurer::disable
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies(
                                "BEMOLE_ADMIN_SESSION"
                        )
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                (request, response, authException) ->
                                        response.sendRedirect(
                                                "/login"
                                        )
                        )
                        .accessDeniedHandler(
                                (request, response, deniedException) ->
                                        response.sendRedirect(
                                                "/acceso-denegado"
                                        )
                        )
                ).addFilterBefore(adminFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
