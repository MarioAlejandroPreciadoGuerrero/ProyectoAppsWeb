package com.example.spa.service;

import com.example.spa.model.Cita;
import com.example.spa.model.EstadoCita;
import com.example.spa.repository.CitaRepository;
import com.example.spa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;




    public List<Cita> obtenerCitasPendientesDeUsuario(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new RuntimeException("El usuario no existe");
        }

        return citaRepository.findByUsuarioIdAndEstado(idUsuario, EstadoCita.PENDIENTE);
    }
}
