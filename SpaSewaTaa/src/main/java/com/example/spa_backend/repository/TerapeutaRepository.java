package com.example.spa_backend.repository;

import com.example.spa_backend.model.Terapeuta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TerapeutaRepository extends JpaRepository<Terapeuta, Long> {
    List<Terapeuta> findByNombreServicio(String servicio);
}
