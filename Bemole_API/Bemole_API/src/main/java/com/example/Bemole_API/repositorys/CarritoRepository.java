package com.example.Bemole_API.repositorys;

import com.example.Bemole_API.models.Carrito;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuarioId(Long usuarioId);

    @EntityGraph(attributePaths = {
            "items",
            "items.producto",
            "items.producto.categoria"
    })
    Optional<Carrito> findDetalleByUsuarioId(Long usuarioId);
}
