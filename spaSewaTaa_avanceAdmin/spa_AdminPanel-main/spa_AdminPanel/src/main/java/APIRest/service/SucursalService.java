package APIRest.service;

import APIRest.exception.ResourceNotFoundException;
import APIRest.model.Sucursal;
import APIRest.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService {
    @Autowired
    private SucursalRepository sucursalRepository;

    public Sucursal obtenerPorId(Long id){
        return this.sucursalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada"));
    }

    public Sucursal obtenerPorNombre(String nombre){
        return this.sucursalRepository.findByNombre(nombre).orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada"));
    }

    public List<Sucursal> obtenerTodas(){
        return this.sucursalRepository.findAll();
    }
}
