package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.application.port.in.AlertaService;
import com.agromatik.cloud.application.port.in.RecomendacionService;
import com.agromatik.cloud.application.port.out.AlertaPort;
import com.agromatik.cloud.domain.enums.Severity;
import com.agromatik.cloud.domain.model.Alerta;
import com.agromatik.cloud.domain.model.SensorData;
import com.agromatik.cloud.infrastructure.config.app.AlertThresholdConfig;
import com.agromatik.cloud.infrastructure.mysql.repository.SpringAlertaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlertaServiceImpl implements AlertaService {
    private final RecomendacionService recomendacionService;
    private final AlertaPort alertaPort;
    private final AlertThresholdConfig thresholdConfig;


    @Override
    public void evaluarAlertas(SensorData data) {
        Map<String, Double> valores = Map.of(
                "generalTemperature", data.getGeneralTemperature(),
                "generalHumidity", data.getGeneralHumidity(),
                "plantsTemperature", data.getPlantsTemperature(),
                "plantsHumidity", data.getPlantsHumidity(),
                "plantsSoilMoisture", safeConvert(data.getPlantsSoilMoisture()),
                "waterPH", data.getWaterPH(),
                "waterTDS", data.getWaterTDS()
        );
        for (var entry : valores.entrySet()) {
            String key = entry.getKey();
            Double valor = entry.getValue();

            // Verificar si el sensor está desconectado
            if (valor != null && valor == -999.9) {
                Alerta alerta = Alerta.builder()
                        .parametro(key)
                        .descripcion("Sensor desconectado: " + key)
                        .valorActual(valor)
                        .umbralMin(null)
                        .umbralMax(null)
                        .timestamp(LocalDateTime.now())
                        .severidad(Severity.CRITICA)
                        .leida(false)
                        .build();

                Alerta alertaGuardada = alertaPort.guardar(alerta);
                recomendacionService.generarRecomendacion(alertaGuardada);
                continue;
            }
            AlertThresholdConfig.Threshold threshold = thresholdConfig.getThresholds().get(key);
            if (threshold == null) continue;

            if (valor < threshold.min() || valor > threshold.max()) {
                Alerta alerta = Alerta.builder()
                        .parametro(key)
                        .descripcion("Valor fuera de umbral: " + key)
                        .valorActual(valor)
                        .umbralMin(threshold.min())
                        .umbralMax(threshold.max())
                        .timestamp(LocalDateTime.now())
                        .severidad(definirSeveridad(valor, threshold))
                        .leida(false)
                        .build();
                Alerta alertaGuardada = alertaPort.guardar(alerta);
                recomendacionService.generarRecomendacion(alertaGuardada);
            }
        }
    }

    private void procesarValorSensor(String parametro, Double valor) {
        // Verificar sensor desconectado
        if (thresholdConfig.isDisconnected(valor)) {
            alertaPort.guardar(Alerta.builder()
                    .parametro(parametro)
                    .descripcion("Sensor desconectado: " + parametro)
                    .valorActual(valor)
                    .umbralMin(null)
                    .umbralMax(null)
                    .timestamp(LocalDateTime.now())
                    .severidad(Severity.CRITICA)
                    .leida(false)
                    .build());
            return;
        }

        AlertThresholdConfig.Threshold threshold = thresholdConfig.getThresholds().get(parametro);
        if (threshold == null) return;

        if (valor < threshold.min() || valor > threshold.max()) {
            alertaPort.guardar(crearAlerta(parametro, valor, threshold));
        }
    }

    @Override
    public Double safeConvert(Integer value) {
        return value == null ? null : value.doubleValue();
    }

    @Override
    public Severity definirSeveridad(Double valor, AlertThresholdConfig.Threshold threshold) {
        double rango = threshold.max() - threshold.min();
        double margen = rango * 0.1;

        if (valor < threshold.min() - margen || valor > threshold.max() + margen) {
            return Severity.ALTA;
        } else if (valor < threshold.min() || valor > threshold.max()) {
            return Severity.MEDIA;
        } else {
            return Severity.BAJA;
        }
    }

    @Override
    public Page<Alerta> obtenerTodas(Pageable pageable) {
        return alertaPort.buscarTodas(pageable);
    }

    @Override
    public Page<Alerta> obtenerPorEstadoLectura(boolean leida, Pageable pageable) {
        return alertaPort.buscarPorLeida(leida, pageable);
    }

    @Override
    public Optional<Alerta> obtenerPorId(Long id) {
        return alertaPort.buscarPorId(id);
    }

    @Override
    public void marcarComoLeida(Long id) {
        alertaPort.actualizarComoLeida(id);
    }

    @Override
    public void marcarTodasComoLeidas() {
        alertaPort.actualizarTodasComoLeidas();
    }

    private Double convertirSeguro(Integer value) {
        return value == null ? null : value.doubleValue();
    }



    private Alerta crearAlerta(String parametro, Double valor, AlertThresholdConfig.Threshold threshold) {
        return Alerta.builder()
                .parametro(parametro)
                .descripcion("Valor fuera de umbral: " + parametro)
                .valorActual(valor)
                .umbralMin(threshold.min())
                .umbralMax(threshold.max())
                .timestamp(LocalDateTime.now())
                .severidad(definirSeveridad(valor, threshold))
                .leida(false)
                .build();
    }

}
