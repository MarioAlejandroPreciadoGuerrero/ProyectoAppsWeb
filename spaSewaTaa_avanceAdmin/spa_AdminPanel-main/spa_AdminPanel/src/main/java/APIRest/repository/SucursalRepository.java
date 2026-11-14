package APIRest.repository;

import APIRest.model.Sucursal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends CrudRepository<Sucursal,Integer> {
    List<Sucursal> findByNombre(String nombre);
}
