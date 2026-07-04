package com.example.Bemole_API.service.mappers;

import com.example.Bemole_API.dto.usuarios.UsuarioResponseDTO;
import com.example.Bemole_API.dto.usuarios.UsuarioResumenDTO;
import com.example.Bemole_API.models.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getRol(),
                usuario.getFechaRegistro()
        );
    }

    public UsuarioResumenDTO toResumenDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new UsuarioResumenDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }
}
