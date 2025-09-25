package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.application.port.in.AlertaService;
import com.agromatik.cloud.application.port.in.RecomendacionService;
import com.agromatik.cloud.application.port.out.AlertaPort;
import com.agromatik.cloud.domain.enums.Severity;
import com.agromatik.cloud.domain.model.Alerta;
import com.agromatik.cloud.domain.model.SensorData;
import com.agromatik.cloud.infrastructure.config.app.AlertThresholdConfig;
import com.agromatik.cloud.infrastructure.mail.AlertNotificationService;
import com.agromatik.cloud.infrastructure.mail.AutoEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlertaServiceImpl implements AlertaService {
    private final RecomendacionService recomendacionService;
    private final AlertaPort alertaPort;
    private final AlertThresholdConfig thresholdConfig;
    private final AlertNotificationService notificationService;
    private final AutoEmailService autoEmailService;

    @Override
    public void evaluarAlertas(SensorData data) {
        System.out.println("=== INICIO EVALUACIÓN ALERTAS ===");
        System.out.println("Datos recibidos: " + data.toString());

        Map<String, Double> valores = Map.of(
                "generalTemperature", data.getGeneralTemperature(),
                "generalHumidity", data.getGeneralHumidity(),
                "plantsTemperature", data.getPlantsTemperature(),
                "plantsHumidity", data.getPlantsHumidity(),
                "plantsSoilMoisture", safeConvert(data.getPlantsSoilMoisture()),
                "waterSoilMoisture", safeConvert(data.getWaterSoilMoisture()),
                "waterPH", data.getWaterPH(),
                "waterTDS", data.getWaterTDS()
        );

        for (var entry : valores.entrySet()) {
            String parametro = entry.getKey(); // ✅ Cambiado de 'key' a 'parametro'
            Double valor = entry.getValue();

            System.out.println("\nProcesando parámetro: " + parametro);
            System.out.println("Valor: " + valor);

            // Verificar si el sensor está desconectado
            if (valor != null && thresholdConfig.isDisconnected(valor)) {
                System.out.println("Sensor desconectado detectado: " + parametro);
                String descripcion = "Sensor desconectado: " + parametro;
                Severity severidad = Severity.CRITICA;

                Alerta alerta = Alerta.builder()
                        .parametro(parametro)
                        .descripcion(descripcion)
                        .valorActual(valor)
                        .umbralMin(null)
                        .umbralMax(null)
                        .timestamp(LocalDateTime.now())
                        .severidad(severidad)
                        .leida(false)
                        .build();

                Alerta alertaGuardada = alertaPort.guardar(alerta);
                recomendacionService.generarRecomendacion(alertaGuardada);

                // ✅ CORREGIDO: Usando variables definidas
                autoEmailService.enviarAlertaAutomatica(parametro, descripcion, valor, severidad.toString());
                continue;
            }

            AlertThresholdConfig.Threshold threshold = thresholdConfig.getThresholds().get(parametro);
            System.out.println("Umbrales para " + parametro + ": min=" +
                    (threshold != null ? threshold.min() : "null") +
                    ", max=" + (threshold != null ? threshold.max() : "null"));

            if (threshold == null) {
                System.out.println("No hay umbrales definidos para " + parametro);
                continue;
            }

            if (valor < threshold.min() || valor > threshold.max()) {
                System.out.println("ALERTA: Valor fuera de umbral para " + parametro);
                String descripcion = "Valor fuera de umbral: " + parametro;
                Severity severidad = definirSeveridad(valor, threshold);

                Alerta alerta = Alerta.builder()
                        .parametro(parametro)
                        .descripcion(descripcion)
                        .valorActual(valor)
                        .umbralMin(threshold.min())
                        .umbralMax(threshold.max())
                        .timestamp(LocalDateTime.now())
                        .severidad(severidad)
                        .leida(false)
                        .build();

                Alerta alertaGuardada = alertaPort.guardar(alerta);
                recomendacionService.generarRecomendacion(alertaGuardada);

                // ✅ CORREGIDO: Envío de email para alerta de umbral
                autoEmailService.enviarAlertaAutomatica(parametro, descripcion, valor, severidad.toString());
            } else {
                System.out.println("Valor dentro de umbrales para " + parametro);
            }
        }
        System.out.println("=== FIN EVALUACIÓN ALERTAS ===");
    }

    // Método simplificado para procesar valores individuales
    private void procesarValorSensor(String parametro, Double valor) {
        if (valor == null) return;

        // Verificar sensor desconectado
        if (thresholdConfig.isDisconnected(valor)) {
            String descripcion = "Sensor desconectado: " + parametro;
            Severity severidad = Severity.CRITICA;

            alertaPort.guardar(Alerta.builder()
                    .parametro(parametro)
                    .descripcion(descripcion)
                    .valorActual(valor)
                    .umbralMin(null)
                    .umbralMax(null)
                    .timestamp(LocalDateTime.now())
                    .severidad(severidad)
                    .leida(false)
                    .build());

            autoEmailService.enviarAlertaAutomatica(parametro, descripcion, valor, severidad.toString());
            return;
        }

        AlertThresholdConfig.Threshold threshold = thresholdConfig.getThresholds().get(parametro);
        if (threshold == null) return;

        if (valor < threshold.min() || valor > threshold.max()) {
            String descripcion = "Valor fuera de umbral: " + parametro;
            Severity severidad = definirSeveridad(valor, threshold);

            alertaPort.guardar(crearAlerta(parametro, valor, threshold));
            autoEmailService.enviarAlertaAutomatica(parametro, descripcion, valor, severidad.toString());
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

    @Override
    public Page<Alerta> obtenerPorSeveridades(List<Severity> severidades, Pageable pageable) {
        return alertaPort.buscarPorSeveridades(severidades, pageable);
    }

    @Override
    public Page<Alerta> obtenerPorEstadoLecturaYSeveridades(boolean leida, List<Severity> severidades, Pageable pageable) {
        return alertaPort.buscarPorLeidaYSeveridades(leida, severidades, pageable);
    }
}