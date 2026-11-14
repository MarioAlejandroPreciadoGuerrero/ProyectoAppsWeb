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
        return this.terapeutaRepository.findByNombre(nombre);
    }

    public Terapeuta obtenerPorId(Long id){
        return this.terapeutaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Terapeuta no encontrado"));
    }

    public List<Terapeuta> obtenerPorEspecialidad(String especialidad){
        return this.terapeutaRepository.findByExpecialidad(especialidad);
    }
    //no creo que el cliente necesite editar o eliminar los terapeutas
}
