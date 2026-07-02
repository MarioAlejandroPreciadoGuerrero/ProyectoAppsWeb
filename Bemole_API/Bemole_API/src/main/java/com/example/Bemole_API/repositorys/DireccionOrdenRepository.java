package com.example.Bemole_API.repositorys;

import com.example.Bemole_API.models.DireccionOrden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DireccionOrdenRepository extends JpaRepository<DireccionOrden, Long> {
    Optional<DireccionOrden> findByOrdenId(Long ordenId);
}
