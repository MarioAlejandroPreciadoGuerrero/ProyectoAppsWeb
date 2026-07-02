package com.example.Bemole_API.contoller;

import com.example.Bemole_API.dto.PaginacionDTO;
import com.example.Bemole_API.dto.producto.ProductoDetalleDTO;
import com.example.Bemole_API.dto.producto.ProductoResumenDTO;
import com.example.Bemole_API.dto.producto.enums.OrdenProducto;
import com.example.Bemole_API.models.Producto;
import com.example.Bemole_API.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/producto")
public class ProductoController {
    @Autowired
    private ProductoService service;

    @GetMapping
    public ResponseEntity<PaginacionDTO<ProductoResumenDTO>> listar(@RequestParam(required = false) String q, @RequestParam(required = false) Long categoriaId, @RequestParam(required = false) BigDecimal precioMin, @RequestParam(required = false) BigDecimal precioMax, @RequestParam(defaultValue = "false") Boolean soloConStock, @RequestParam(required = false) OrdenProducto orden, @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "12") int tamano) {
        return ResponseEntity.ok(service.listarCatalogo(
                        q,
                        categoriaId,
                        precioMin,
                        precioMax,
                        soloConStock,
                        orden,
                        pagina,
                        tamano
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDetalleDTO> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(service.obtenerDetalle(id));
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
