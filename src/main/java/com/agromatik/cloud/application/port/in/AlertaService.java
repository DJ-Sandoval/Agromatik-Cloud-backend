package com.agromatik.cloud.application.port.in;

import com.agromatik.cloud.domain.enums.Severity;
import com.agromatik.cloud.domain.model.Alerta;
import com.agromatik.cloud.domain.model.SensorData;
import com.agromatik.cloud.infrastructure.config.app.AlertThresholdConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

// UseCase
public interface AlertaService {
    void evaluarAlertas(SensorData data);
    Double safeConvert(Integer value);
    Severity definirSeveridad(Double valor, AlertThresholdConfig.Threshold threshold);
    Page<Alerta> obtenerTodas(Pageable pageable);
    Page<Alerta> obtenerPorEstadoLectura(boolean leida, Pageable pageable);
    Optional<Alerta> obtenerPorId(Long id);
    void marcarComoLeida(Long id);
    void marcarTodasComoLeidas();
    Page<Alerta> obtenerPorSeveridades(List<Severity> severidades, Pageable pageable);
    Page<Alerta> obtenerPorEstadoLecturaYSeveridades(boolean leida, List<Severity> severidades, Pageable pageable);
}
