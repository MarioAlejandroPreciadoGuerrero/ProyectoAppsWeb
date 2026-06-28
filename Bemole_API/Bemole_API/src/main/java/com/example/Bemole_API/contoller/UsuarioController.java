package com.example.Bemole_API.contoller;

import com.example.Bemole_API.models.Usuario;
import com.example.Bemole_API.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Usuario crear(@RequestBody Usuario usuario){
        return service.registrarUsuario(usuario);
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
