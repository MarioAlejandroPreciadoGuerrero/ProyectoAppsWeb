package APIRest.repository;

import APIRest.model.Sucursal;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SucursalRepository extends CrudRepository<Sucursal,Integer> {
    List<Sucursal> findByNombre(String nombre);

}
