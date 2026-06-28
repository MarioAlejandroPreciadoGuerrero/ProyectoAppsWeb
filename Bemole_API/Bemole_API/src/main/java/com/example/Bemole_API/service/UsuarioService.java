package com.example.Bemole_API.service;

import com.example.Bemole_API.models.Usuario;
import com.example.Bemole_API.repositorys.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> listarUsuarios(){
        return repository.findAll();
    }

    public Usuario obtenerPorId(Long id){
        return repository.findById(id).orElse(null);
    }

    public Usuario registrarUsuario(Usuario usuario){
        return repository.save(usuario);
    }

    public Usuario actualizarUsuarioParcial(Long id, Usuario usuarioParcial) {
        return repository.findById(id).map(usuario -> {
            if (usuarioParcial.getNombre() != null) usuario.setNombre(usuarioParcial.getNombre());
            if (usuarioParcial.getEmail() != null) usuario.setEmail(usuarioParcial.getEmail());
            if (usuarioParcial.getRol() != null) usuario.setRol(usuarioParcial.getRol());

            return repository.save(usuario);

        }).orElse(null);
    }

    public Usuario actualizarPassword(Long id, Usuario usuarioParcial){
        return repository.findById(id).map(usuario -> {
            if (usuarioParcial.getPassword() != null) usuario.setPassword(usuarioParcial.getPassword());

            return repository.save(usuario);

        }).orElse(null);
    }

    public void eliminarUsuario(Long id){
        repository.deleteById(id);
    }
}
