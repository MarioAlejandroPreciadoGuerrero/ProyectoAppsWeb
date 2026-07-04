package com.example.Bemole_API.contoller;

import com.example.Bemole_API.dto.categoria.CategoriaResumenDTO;
import com.example.Bemole_API.models.Categoria;
import com.example.Bemole_API.service.CategoriaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categoria")
@AllArgsConstructor
public class CategoriaController {

    @Autowired
    private final CategoriaService service;

    @GetMapping
    public ResponseEntity<List<CategoriaResumenDTO>> listar() {
        return ResponseEntity.ok(service.listarPublicas());
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
