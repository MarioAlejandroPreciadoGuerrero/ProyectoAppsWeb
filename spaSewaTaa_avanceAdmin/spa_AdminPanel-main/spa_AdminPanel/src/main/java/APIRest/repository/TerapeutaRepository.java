package APIRest.repository;

import APIRest.model.Terapeuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerapeutaRepository extends JpaRepository<Terapeuta, Long> {
    List<Terapeuta> findByNombre(String nombre);
    List<Terapeuta> findByExpecialidad(String especialidad);
}
