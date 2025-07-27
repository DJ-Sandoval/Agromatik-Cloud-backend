package com.agromatik.cloud.infrastructure.mysql.adapter;

import com.agromatik.cloud.domain.model.Alerta;
import com.agromatik.cloud.infrastructure.mysql.repository.SpringAlertaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AlertaMonitor implements CommandLineRunner {
    private final SpringAlertaRepository alertaRepository;

    @Override
    public void run(String... args) {
        new Thread(() -> {
            List<Long> mostradas = new ArrayList<>();
            while (true) {
                List<Alerta> nuevas = alertaRepository.findByLeida(false, Pageable.unpaged())
                        .getContent().stream()
                        .filter(a -> !mostradas.contains(a.getId()))
                        .toList();

                nuevas.forEach(alerta -> {
                    System.out.println("🔔 ALERTA NUEVA: " + alerta.getParametro() +
                            " → " + alerta.getValorActual() + " (" + alerta.getSeveridad() + ")");
                    mostradas.add(alerta.getId());
                });

                try {
                    Thread.sleep(5000); // cada 3 segundos
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
}

