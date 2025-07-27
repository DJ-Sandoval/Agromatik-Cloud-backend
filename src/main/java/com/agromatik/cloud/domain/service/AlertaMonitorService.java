package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.domain.model.Alerta;
import com.agromatik.cloud.infrastructure.mysql.repository.SpringAlertaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class AlertaMonitorService {
    private final SpringAlertaRepository alertaRepository;

    private final List<Alerta> nuevasAlertas = new CopyOnWriteArrayList<>();

    @Scheduled(fixedDelay = 5000)
    public void verificarNuevasAlertas() {
        List<Alerta> noLeidas = alertaRepository.findByLeida(false, Pageable.unpaged()).getContent();
        for (Alerta alerta : noLeidas) {
            boolean yaExiste = nuevasAlertas.stream().anyMatch(a -> a.getId().equals(alerta.getId()));
            if (!yaExiste) {
                nuevasAlertas.add(alerta);
            }
        }
    }

    public List<Alerta> obtenerNuevasAlertasYLimpiar() {
        List<Alerta> copia = new ArrayList<>(nuevasAlertas);
        nuevasAlertas.clear();
        return copia;
    }
}

