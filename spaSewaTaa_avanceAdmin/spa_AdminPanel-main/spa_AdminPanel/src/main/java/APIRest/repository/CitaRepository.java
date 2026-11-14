package APIRest.repository;

import APIRest.model.Cita;
import com.example.spa_AdminPanel.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findCitasByEstado(EstadoCita estado);

    @Query("SELECT c FROM Cita c WHERE c.cliente.id_cliente = :idCliente")
    List<Cita> findCitasByClienteId(@Param("idCliente") Long idCliente);

    @Query("""
            SELECT c FROM Cita c
            WHERE c.cliente.id_cliente = :idCliente
              AND c.estado = :estado
            """)
    List<Cita> findCitasByClienteIdAndEstado(
            @Param("idCliente") Long idCliente,
            @Param("estado") EstadoCita estado
    );

    @Query("""
            SELECT c FROM Cita c
            WHERE c.id_cita = :idCita
              AND c.cliente.id_cliente = :idCliente
            """)
    Optional<Cita> findCitaByIdAndClienteId(
            @Param("idCita") Long idCita,
            @Param("idCliente") Long idCliente
    );
}
