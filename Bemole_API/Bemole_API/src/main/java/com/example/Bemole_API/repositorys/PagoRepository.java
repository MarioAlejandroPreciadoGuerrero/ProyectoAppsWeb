package com.example.Bemole_API.repositorys;

import com.example.Bemole_API.models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByOrdenId(Long ordenId);

    Optional<Pago> findByMercadoPagoPaymentId(String mercadoPagoPaymentId);

    Optional<Pago> findByPreferenceId(String preferenceId);
}
