package com.example.Bemole_API.contoller;

import com.example.Bemole_API.models.Producto;
import com.example.Bemole_API.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/producto")
public class ProductoController {
    @Autowired
    private ProductoService service;

    @GetMapping()
    public List<Producto> listar(){
        return service.listarProductos();
    }

    @GetMapping("/{id}")
    public Producto obtenerPorId(@PathVariable Long id){
        return service.obtenerPorId(id);
    }

    @PostMapping
    public Producto crear(@RequestBody Producto producto){
        return service.crearProducto(producto);
    }

    @PutMapping("/{id}")
    public Producto actualizarCompleto(@PathVariable Long id, @RequestBody Producto producto){
        return service.actualizarProductoCompleto(id,producto);
    }

    @PatchMapping("/{id}")
    public Producto actualizarParcial(@PathVariable Long id, @RequestBody Producto producto){
        return service.actualizarProductoParcial(id,producto);
    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id){
        service.eliminarProducto(id);
    }
}
