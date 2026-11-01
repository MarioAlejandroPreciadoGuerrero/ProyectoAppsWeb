package com.example.spa.service;

import com.example.spa.model.NombreRol;
import com.example.spa.model.Rol;
import com.example.spa.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public Rol buscarPorNombre(NombreRol nombre) {
        return rolRepository.findByNombre(nombre).orElse(null);
    }

}
