package com.example.Bemole_API.repositorys;

import com.example.Bemole_API.models.Carrito;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuarioId(Long usuarioId);

    @Query("""
        SELECT DISTINCT c
        FROM Carrito c
        LEFT JOIN FETCH c.items i
        LEFT JOIN FETCH i.producto p
        LEFT JOIN FETCH p.categoria
        WHERE c.usuario.id = :usuarioId
    """)
    Optional<Carrito> findDetalleByUsuarioId(
            @Param("usuarioId") Long usuarioId
    );
}
