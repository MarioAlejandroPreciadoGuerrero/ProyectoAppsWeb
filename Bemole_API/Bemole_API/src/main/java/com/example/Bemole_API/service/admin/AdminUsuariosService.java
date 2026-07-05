package com.example.Bemole_API.service.admin;

import com.example.Bemole_API.dto.admin.response.UsuarioAdminResponseDTO;
import com.example.Bemole_API.models.Usuario;
import com.example.Bemole_API.repositorys.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminUsuariosService {
    private final UsuarioRepository repository;

    public Page<UsuarioAdminResponseDTO> listar( Pageable pageable){
        return repository.findAll(pageable).map(this::convertir);
    }

    private UsuarioAdminResponseDTO convertir(Usuario usuario) {
        return new UsuarioAdminResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol().name(),
                usuario.getFechaRegistro()
        );
    }
}
