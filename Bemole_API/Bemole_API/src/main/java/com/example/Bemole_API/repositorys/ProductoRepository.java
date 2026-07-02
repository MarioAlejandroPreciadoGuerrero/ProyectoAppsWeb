package com.example.Bemole_API.repositorys;

import com.example.Bemole_API.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {
    long countByCategoriaIdAndActivoTrue(Long categoriaId);
    boolean existsByNombreIgnoreCase(String nombre);
}
