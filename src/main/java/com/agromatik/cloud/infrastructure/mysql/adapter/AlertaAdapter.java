package com.agromatik.cloud.infrastructure.mysql.adapter;

import com.agromatik.cloud.application.port.out.AlertaPort;
import com.agromatik.cloud.domain.model.Alerta;
import com.agromatik.cloud.infrastructure.mysql.repository.SpringAlertaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AlertaAdapter implements AlertaPort {
    private final SpringAlertaRepository alertaRepository;

    @Override
    public Alerta guardar(Alerta alerta) {
        return alertaRepository.save(alerta);
    }

    @Override
    public Page<Alerta> buscarTodas(Pageable pageable) {
        return alertaRepository.findAll(pageable);
    }

    @Override
    public Page<Alerta> buscarPorLeida(boolean leida, Pageable pageable) {
        return alertaRepository.findByLeida(leida, pageable);
    }

    @Override
    public Optional<Alerta> buscarPorId(Long id) {
        return alertaRepository.findById(id);
    }

    @Override
    public void actualizarComoLeida(Long id) {
        alertaRepository.findById(id).ifPresent(alerta -> {
            alerta.setLeida(true);
            alertaRepository.save(alerta);
        });
    }

    @Override
    public void actualizarTodasComoLeidas() {
        List<Alerta> alertasNoLeidas = alertaRepository.findByLeida(false);
        alertasNoLeidas.forEach(alerta -> alerta.setLeida(true));
        alertaRepository.saveAll(alertasNoLeidas);
    }
}
