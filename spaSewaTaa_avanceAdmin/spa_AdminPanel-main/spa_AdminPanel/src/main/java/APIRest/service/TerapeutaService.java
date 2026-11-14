package APIRest.service;

import APIRest.exception.ResourceNotFoundException;
import APIRest.model.Servicio;
import APIRest.model.Terapeuta;
import APIRest.repository.TerapeutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerapeutaService {
    @Autowired
    private TerapeutaRepository terapeutaRepository;

    public TerapeutaService(TerapeutaRepository terapeutaRepository) {
        this.terapeutaRepository = terapeutaRepository;
    }

    public List<Terapeuta> obtenerTodos() {
        return terapeutaRepository.findAll();
    }

    public List<Terapeuta> obtenerPorNombre(String nombre){
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        return this.terapeutaRepository.buscarPorNombre(nombre.trim());
    }

    public Terapeuta obtenerPorId(Long id){
        return this.terapeutaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Terapeuta no encontrado"));
    }

    public List<Terapeuta> obtenerPorServicio(Long idServicio){
        return this.terapeutaRepository.findByServicioId(idServicio);
    }

    public List<Terapeuta> obtenerPorSucursal(Long idSucursal){
        return this.terapeutaRepository.findBySucursalId(idSucursal);
    }

    public List<Terapeuta> obtenerPorServicioYSucursal(Long idServicio, Long idSucursal){
        return this.terapeutaRepository.findByServicioIdAndSucursalId(idServicio, idSucursal);
    }
}
