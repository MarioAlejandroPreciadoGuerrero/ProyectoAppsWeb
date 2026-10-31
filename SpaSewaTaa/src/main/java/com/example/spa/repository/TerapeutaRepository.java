package com.example.spa.repository;

import com.example.spa.model.Terapeuta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TerapeutaRepository extends JpaRepository<Terapeuta, Long> {
    List<Terapeuta> findDistinctByServicios_NombreIgnoreCase(String servicio);
}
