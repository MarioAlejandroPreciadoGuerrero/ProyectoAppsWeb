package APIRest.service;

import APIRest.model.Servicio;
import APIRest.repository.TerapeutaRepository;

import java.util.List;

public class TerapeutaService {
    private final TerapeutaRepository terapeutaRepository;
    public TerapeutaService(TerapeutaRepository terapeutaRepository) {
        this.terapeutaRepository = terapeutaRepository;
    }
    public List<Servicio> findAll() {
        return terapeutaRepository.findAll();
    }


}
