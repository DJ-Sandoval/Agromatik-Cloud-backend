package com.agromatik.cloud.infrastructure.mysql.adapter;

import com.agromatik.cloud.application.port.out.AlertaPort;
import com.agromatik.cloud.application.port.out.RecomendacionPort;
import com.agromatik.cloud.domain.model.Alerta;
import com.agromatik.cloud.domain.model.Recomendacion;
import com.agromatik.cloud.infrastructure.mysql.repository.SpringRecomendacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecomendacionAdapter implements RecomendacionPort {
    private final SpringRecomendacionRepository recomendacionRepository;

    @Override
    public Recomendacion guardar(Recomendacion recomendacion) {
        return recomendacionRepository.save(recomendacion);
    }

    @Override
    public List<Recomendacion> buscarPorAlertaId(Long alertaId) {
        return recomendacionRepository.findByAlertaId(alertaId);
    }

    @Override
    public List<Recomendacion> buscarNoImplementadas() {
        return recomendacionRepository.findByImplementadaFalse();
    }

    @Override
    public void actualizarComoImplementada(Long id) {
        recomendacionRepository.findById(id).ifPresent(recomendacion -> {
            recomendacion.setImplementada(true);
            recomendacionRepository.save(recomendacion);
        });
    }

    @Override
    public Optional<Recomendacion> buscarPorId(Long id) {
        return recomendacionRepository.findById(id);
    }

    @Override
    public Page<Recomendacion> buscarTodas(Pageable pageable) {
        return recomendacionRepository.findAll(pageable);
    }


}
