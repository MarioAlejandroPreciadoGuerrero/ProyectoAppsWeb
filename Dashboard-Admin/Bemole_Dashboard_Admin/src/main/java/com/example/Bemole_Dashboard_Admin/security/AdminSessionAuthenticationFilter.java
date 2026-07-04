package com.example.Bemole_Dashboard_Admin.security;

import com.example.Bemole_Dashboard_Admin.dto.admin.AdminSesionDTO;
import com.example.Bemole_Dashboard_Admin.session.AdminSessionKeys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class AdminSessionAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        boolean noExisteAutenticacion = SecurityContextHolder.getContext().getAuthentication() == null;

        if (session != null && noExisteAutenticacion) {
            Object atributo = session.getAttribute(AdminSessionKeys.ADMIN_SESSION);

            if (atributo instanceof AdminSesionDTO admin) {
                UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(admin, null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_ADMIN"
                                )
                        )
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }


}
