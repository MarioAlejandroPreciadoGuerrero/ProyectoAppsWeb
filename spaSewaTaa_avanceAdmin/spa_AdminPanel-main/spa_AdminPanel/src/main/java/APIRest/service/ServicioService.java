package APIRest.service;

import APIRest.exception.BusinessRulesException;
import APIRest.exception.ResourceNotFoundException;
import APIRest.model.Servicio;
import APIRest.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {
    @Autowired
    private ServicioRepository servicioRepository;

    public List<Servicio> obtenerTodos(){
        return this.servicioRepository.findAll();
    }

    public Servicio obtenerPorId(Long id){
        return this.servicioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El servicio no existe"));
    }

    //TODO obtener servicio por nombre
    public Servicio obtenerPorNombre(String nombre){
        return null;
    }

    //TODO obtener servicios por categoria
    public List<Servicio> obtenerPorCategoria(String categoria){
        return null;
    }

    public Servicio crear(Servicio servicio){
        return this.servicioRepository.save(servicio);
    }

    public void eliminar(Servicio servicio){
        servicioRepository.delete(servicio);
    }

    public Servicio actualizarServicioCompleto(Long id, Servicio servicioActualizado){
        return servicioRepository.findById(id).map(servicio -> {
           servicio.setNombre(servicioActualizado.getNombre());
           servicio.setCategoria(servicioActualizado.getCategoria());
           servicio.setDescripcion(servicioActualizado.getDescripcion());
           servicio.setDuracion(servicioActualizado.getDuracion());
           servicio.setPrecio(servicioActualizado.getPrecio());
           servicio.setUrl_imagen(servicioActualizado.getUrl_imagen());
           servicio.setTerapeutas(servicioActualizado.getTerapeutas());
            return servicioRepository.save(servicio);
        }).orElseThrow(() -> new BusinessRulesException("Error en el formato del nuevo servicio"));
    }

    public Servicio actualizarServicioParcial(Long id, Servicio servicioParcial){
        return servicioRepository.findById(id).map(servicio -> {
            if(servicioParcial.getNombre() != null) servicio.setNombre(servicioParcial.getNombre());
            if(servicioParcial.getCategoria() != null) servicio.setCategoria(servicioParcial.getCategoria());
            if(servicioParcial.getDescripcion() != null) servicio.setDescripcion(servicioParcial.getDescripcion());
            if(servicioParcial.getDuracion() != null) servicio.setDuracion(servicioParcial.getDuracion());
            if(servicioParcial.getPrecio() != null) servicio.setPrecio(servicioParcial.getPrecio());
            if(servicioParcial.getUrl_imagen() != null) servicio.setUrl_imagen(servicioParcial.getUrl_imagen());
            if(servicioParcial.getTerapeutas() != null) servicio.setTerapeutas(servicioParcial.getTerapeutas());
            return servicioRepository.save(servicio);
        }).orElseThrow(() -> new BusinessRulesException("Error en el formato del nuevo servicio"));
    }

}

