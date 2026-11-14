package APIRest.service;

import APIRest.exception.ResourceNotFoundException;
import APIRest.model.Pago;
import APIRest.repository.PagoRepository;
import com.example.spa_AdminPanel.model.EstadoPago;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public List<Pago> obtenerMisPagos(Long idCliente){
        return this.pagoRepository.findPagosByIdCliente(idCliente);
    }

    public List<Pago> obtenerMisPagosPorEstado(Long idCliente, EstadoPago estado){
        return this.pagoRepository.findPagosByIdClienteAndEstado(idCliente, estado);
    }

    public Pago obtenerMiPago(Long idPago, Long idCliente){
        return this.pagoRepository.findPagoByIdAndIdCliente(idPago, idCliente)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));
    }
}
