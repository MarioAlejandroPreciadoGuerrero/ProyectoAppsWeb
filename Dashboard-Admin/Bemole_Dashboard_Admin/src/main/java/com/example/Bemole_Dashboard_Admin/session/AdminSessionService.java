package com.example.Bemole_Dashboard_Admin.session;

import com.example.Bemole_Dashboard_Admin.dto.admin.AdminSesionDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class AdminSessionService {
    public AdminSesionDTO obtenerAdmin(HttpSession session) {
        Object atributo = session.getAttribute(AdminSessionKeys.ADMIN_SESSION);

        if (!(atributo instanceof AdminSesionDTO admin)) {
            throw new IllegalStateException(
                    "No existe una sesión administrativa."
            );
        }

        return admin;
    }

    public String obtenerToken(HttpSession session) {
        String token = obtenerAdmin(session).getToken();

        if (token == null || token.isBlank()) {
            throw new IllegalStateException(
                    "La sesión administrativa no contiene un JWT."
            );
        }

        return token;
    }
}
