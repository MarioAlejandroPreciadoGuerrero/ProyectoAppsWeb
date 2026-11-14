package APIRest.repository;

import APIRest.model.Terapeuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerapeutaRepository extends JpaRepository<Terapeuta, Long> {

    @Query("""
            SELECT DISTINCT t FROM Terapeuta t
            WHERE LOWER(t.usuario.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
            """)
    List<Terapeuta> buscarPorNombre(@Param("nombre") String nombre);

    @Query("""
            SELECT DISTINCT t FROM Terapeuta t
            JOIN t.servicios s
            WHERE s.id = :idServicio
            """)
    List<Terapeuta> findByServicioId(@Param("idServicio") Long idServicio);

    @Query("""
            SELECT t FROM Terapeuta t
            WHERE t.sucursal.id_sucursal = :idSucursal
            """)
    List<Terapeuta> findBySucursalId(@Param("idSucursal") Long idSucursal);

    @Query("""
            SELECT DISTINCT t FROM Terapeuta t
            JOIN t.servicios s
            WHERE s.id = :idServicio
              AND t.sucursal.id_sucursal = :idSucursal
            """)
    List<Terapeuta> findByServicioIdAndSucursalId(
            @Param("idServicio") Long idServicio,
            @Param("idSucursal") Long idSucursal
    );
}
