package APIRest.service;

import APIRest.model.Servicio;
import APIRest.repository.TerapeutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerapeutaService {
    @Autowired
    private TerapeutaRepository terapeutaRepository;

    public TerapeutaService(TerapeutaRepository terapeutaRepository) {
        this.terapeutaRepository = terapeutaRepository;
    }

    public List<Servicio> findAll() {
        return terapeutaRepository.findAll();
    }



}
