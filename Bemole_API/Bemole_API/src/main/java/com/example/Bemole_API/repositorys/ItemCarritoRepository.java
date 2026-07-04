package com.example.Bemole_API.repositorys;

import com.example.Bemole_API.models.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    Optional<ItemCarrito> findByCarritoIdAndProductoId(
            Long carritoId,
            Long productoId
    );

    Optional<ItemCarrito> findByIdAndCarritoUsuarioId(
            Long itemId,
            Long usuarioId
    );
}
