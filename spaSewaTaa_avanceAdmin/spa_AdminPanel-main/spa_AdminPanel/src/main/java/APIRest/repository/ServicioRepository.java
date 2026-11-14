package APIRest.repository;

import APIRest.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    Optional<Servicio> findByNombre(String nombre);
    List<Servicio> findByCategoriaIgnoreCase(String categoria);

    @Query("SELECT DISTINCT s.categoria FROM Servicio s ORDER BY s.categoria")
    List<String> findCategorias();

    @Query("""
            SELECT s FROM Servicio s
            WHERE LOWER(s.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
            """)
    List<Servicio> buscarPorNombre(@Param("termino") String termino);

}
