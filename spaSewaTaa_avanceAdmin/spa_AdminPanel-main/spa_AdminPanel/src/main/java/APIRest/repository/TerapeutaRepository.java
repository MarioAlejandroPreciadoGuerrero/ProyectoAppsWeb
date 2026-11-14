package APIRest.repository;

import APIRest.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerapeutaRepository extends JpaRepository<Servicio,Long> {
}
