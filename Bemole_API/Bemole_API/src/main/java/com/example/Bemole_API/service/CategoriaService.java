package com.example.Bemole_API.service;

import com.example.Bemole_API.models.Categoria;
import com.example.Bemole_API.repositorys.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository repository;

    public List<Categoria> listarCategorias(){
        return repository.findAll();
    }

    public Categoria obtenerPorId(Long id){
        return repository.findById(id).orElse(null);
    }

    public Categoria crearCategoria(Categoria categoria){
        return repository.save(categoria);
    }

    public Categoria editarCategoria(Long id, Categoria categoriaParcial) {
        return repository.findById(id).map(categoria -> {
            if (categoriaParcial.getNombre() != null) categoria.setNombre(categoriaParcial.getNombre());
            if (categoriaParcial.getDescripcion() != null) categoria.setDescripcion(categoriaParcial.getDescripcion());

            return repository.save(categoria);

        }).orElse(null);
    }

    public void eliminarCategoria(Long id){
        repository.deleteById(id);
    }
}
