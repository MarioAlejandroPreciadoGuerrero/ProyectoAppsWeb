package com.example.Bemole_API.repositorys;

import com.example.Bemole_API.enums.EstadoOrden;
import com.example.Bemole_API.models.Orden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
    Page<Orden> findByUsuarioId(
            Long usuarioId,
            Pageable pageable
    );

    Page<Orden> findByUsuarioIdAndEstado(
            Long usuarioId,
            EstadoOrden estado,
            Pageable pageable
    );

    Optional<Orden> findByIdAndUsuarioId(
            Long ordenId,
            Long usuarioId
    );

    boolean existsByNumero(String numero);
}
