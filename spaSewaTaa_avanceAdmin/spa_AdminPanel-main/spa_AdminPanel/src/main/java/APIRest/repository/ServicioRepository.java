package APIRest.repository;

import APIRest.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    List<Servicio> findByNombre(String nombre);
    List<Servicio> findByCategoria(String categoria);

}
