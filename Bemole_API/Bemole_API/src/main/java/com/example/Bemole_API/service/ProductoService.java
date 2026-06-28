package com.example.Bemole_API.service;

import com.example.Bemole_API.models.Producto;
import com.example.Bemole_API.repositorys.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository repository;

    public List<Producto> listarProductos(){
        return repository.findAll();
    }

    public Producto obtenerPorId(Long id){
        return repository.findById(id).orElse(null);
    }

    public Producto crearProducto(Producto producto){
        return repository.save(producto);
    }

    public Producto actualizarProductoCompleto(Long id, Producto productoActualizado){
        return repository.findById(id).map(producto -> {
           producto.setNombre(productoActualizado.getNombre());
           producto.setActivo(productoActualizado.getActivo());
           producto.setCategoria(productoActualizado.getCategoria());
           producto.setDescripcion(productoActualizado.getDescripcion());
           producto.setPrecio(productoActualizado.getPrecio());
           producto.setStock(productoActualizado.getStock());
           return repository.save(producto);
        }).orElse(null);
    }

    public Producto actualizarProductoParcial(Long id, Producto productoParcial) {
        return repository.findById(id).map(producto -> {
            if (productoParcial.getNombre() != null) producto.setNombre(productoParcial.getNombre());
            if (productoParcial.getCategoria() != null) producto.setCategoria(productoParcial.getCategoria());
            if (productoParcial.getActivo() != null) producto.setActivo(productoParcial.getActivo());
            if (productoParcial.getPrecio() != null) producto.setPrecio(productoParcial.getPrecio());
            if (productoParcial.getStock() != null) producto.setStock(productoParcial.getStock());
            if (productoParcial.getDescripcion() != null) producto.setDescripcion(productoParcial.getDescripcion());

            return repository.save(producto);

        }).orElse(null);
    }

    public void eliminarProducto(Long id){
        repository.deleteById(id);
    }


}
