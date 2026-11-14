package APIRest.repository;

import APIRest.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal,Long> {
    Optional<Sucursal> findByNombre(String nombre);

    @Query("SELECT s FROM Sucursal s WHERE s.codigo_postal = :codigoPostal")
    List<Sucursal> findByCodigoPostal(@Param("codigoPostal") String codigoPostal);

}
