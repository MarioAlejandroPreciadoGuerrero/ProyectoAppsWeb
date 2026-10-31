package com.example.spa.repository;

import com.example.spa.model.Cita;
import com.example.spa.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByUsuarioIdAndEstado(Long idUsuario, EstadoCita estado);
}
