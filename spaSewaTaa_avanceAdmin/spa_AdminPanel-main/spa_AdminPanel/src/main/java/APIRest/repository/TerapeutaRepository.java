package APIRest.repository;

import APIRest.model.Servicio;
import APIRest.model.Terapeuta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TerapeutaRepository extends JpaRepository<Servicio,Long> {
    List<Terapeuta> findByNombre(String nombre);

}
