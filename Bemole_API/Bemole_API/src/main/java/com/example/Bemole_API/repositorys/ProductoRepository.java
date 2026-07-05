package com.example.Bemole_API.repositorys;

import com.example.Bemole_API.models.Producto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT p
        FROM Producto p
        WHERE p.id = :id
        """)
    Optional<Producto> findByIdForUpdate(@Param("id") Long id);
    long countByCategoriaIdAndActivoTrue(Long categoriaId);
    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);

    boolean existsByCategoriaId(Long categoriaId);

    long countByCategoriaId(Long categoriaId);
}
