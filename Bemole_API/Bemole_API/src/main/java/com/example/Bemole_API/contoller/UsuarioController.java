package com.example.Bemole_API.contoller;

import com.example.Bemole_API.dto.usuarios.UsuarioRegistroDTO;
import com.example.Bemole_API.dto.usuarios.UsuarioResponseDTO;
import com.example.Bemole_API.dto.usuarios.UsuarioResumenDTO;
import com.example.Bemole_API.models.Usuario;
import com.example.Bemole_API.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService service;

    @GetMapping()
    public List<Usuario> listar(){
        return service.listarUsuarios();
    }

    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable Long id){
        return service.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody UsuarioRegistroDTO usuarioDTO){
        UsuarioResponseDTO usuarioRegistrado= service.registrarUsuario(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRegistrado);
    }

    @PatchMapping("/{id}")
    public Usuario actualizarParcial(@PathVariable Long id, @RequestBody Usuario usuario){
        return service.actualizarUsuarioParcial(id,usuario);
    }

    @PatchMapping("password/{id}")
    public Usuario actualizarPassword(@PathVariable Long id, @RequestBody Usuario usuario){
        return service.actualizarPassword(id,usuario);
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id){
        service.eliminarUsuario(id);
    }
}
