package APIRest.repository;

import APIRest.model.Pago;
import com.example.spa_AdminPanel.model.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    @Query("""
            SELECT p FROM Pago p
            WHERE p.cita.cliente.id_cliente = :idCliente
            ORDER BY p.fecha_pago DESC
            """)
    List<Pago> findPagosByIdCliente(@Param("idCliente") Long idCliente);

    @Query("""
            SELECT p FROM Pago p
            WHERE p.cita.cliente.id_cliente = :idCliente
              AND p.estado_pago = :estado
            ORDER BY p.fecha_pago DESC
            """)
    List<Pago> findPagosByIdClienteAndEstado(
            @Param("idCliente") Long idCliente,
            @Param("estado") EstadoPago estado
    );

    @Query("""
            SELECT p FROM Pago p
            WHERE p.id_pago = :idPago
              AND p.cita.cliente.id_cliente = :idCliente
            """)
    Optional<Pago> findPagoByIdAndIdCliente(
            @Param("idPago") Long idPago,
            @Param("idCliente") Long idCliente
    );
}
