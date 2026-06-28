package com.example.Bemole_API.contoller;

import com.example.Bemole_API.models.Categoria;
import com.example.Bemole_API.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @GetMapping()
    public List<Categoria> listar(){
        return service.listarCategorias();
    }

    @GetMapping("/{id}")
    public Categoria obtenerPorId(@PathVariable Long id){
        return service.obtenerPorId(id);
    }

    @PostMapping
    public Categoria crear(@RequestBody Categoria categoria){
        return service.crearCategoria(categoria);
    }

    @PatchMapping("/{id}")
    public Categoria editar(@PathVariable Long id, @RequestBody Categoria categoria){
        return service.editarCategoria(id,categoria);
    }

    @DeleteMapping("/{id}")
    public void eliminarCategoria(@PathVariable Long id){
        service.eliminarCategoria(id);
    }
}
