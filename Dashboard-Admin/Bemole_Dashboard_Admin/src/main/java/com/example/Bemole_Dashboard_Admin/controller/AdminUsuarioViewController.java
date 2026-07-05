package com.example.Bemole_Dashboard_Admin.controller;

import com.example.Bemole_Dashboard_Admin.client.AdminUsuarioApiClient;
import com.example.Bemole_Dashboard_Admin.dto.admin.usuario.UsuarioAdminResponseDTO;
import com.example.Bemole_Dashboard_Admin.dto.PaginacionDTO;
import com.example.Bemole_Dashboard_Admin.session.AdminSessionService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/usuarios")
@AllArgsConstructor
public class AdminUsuarioViewController {
    private final AdminUsuarioApiClient usuarioApiClient;
    private final AdminSessionService sessionService;

    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "10") int tamano, HttpSession session, Model model) {
        pagina = Math.max(pagina, 0);

        if (tamano < 1 || tamano > 50) {
            tamano = 10;
        }

        PaginacionDTO<UsuarioAdminResponseDTO> resultado = usuarioApiClient.listar(sessionService.obtenerToken(session), pagina, tamano);

        model.addAttribute("paginaUsuarios", resultado);

        model.addAttribute("usuarios", resultado != null ? resultado.getContent() : java.util.List.of());

        return "admin/usuarios/lista";
    }
}
