package com.example.Bemole_API.repositorys;

import com.example.Bemole_API.enums.EstadoOrden;
import com.example.Bemole_API.enums.EstadoPago;
import com.example.Bemole_API.models.Orden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

    @EntityGraph(attributePaths = {"items", "items.producto"})
    Page<Orden> findByUsuarioId(Long usuarioId, Pageable pageable);

    @EntityGraph(attributePaths = {"items", "items.producto"})
    Page<Orden> findByUsuarioIdAndEstado(Long usuarioId, EstadoOrden estado, Pageable pageable);

    @EntityGraph(attributePaths = {"items", "items.producto"})
    Page<Orden> findByEstado(EstadoOrden estado, Pageable pageable);

    @EntityGraph(attributePaths = {"items", "items.producto"})
    Optional<Orden> findConItemsById(Long id);

    Optional<Orden> findByIdAndUsuarioId(Long ordenId, Long usuarioId);

    boolean existsByNumero(String numero);

    long countByEstado(EstadoOrden estado);

    //Suma las ventas menos de las que tienen estado cancelado
    @Query("""
       SELECT COALESCE(SUM(o.total), 0)
       FROM Orden o
       WHERE o.estado <> :estadoCancelado
       """)
    BigDecimal sumarVentasExceptoEstado(@Param("estadoCancelado") EstadoOrden estadoCancelado);
    Optional<Orden> findFirstByUsuario_IdAndEstadoPagoOrderByFechaDesc(
            Long usuarioId,
            EstadoPago estadoPago
    );
    
}
