package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.application.port.in.AlertaService;
import com.agromatik.cloud.application.port.in.SensorDataUseCase;
import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorDataGeneratorService {
    private final Random random = new Random();
    private final SensorDataUseCase sensorDataUseCase;
    private final AlertaService alertaService;

    public SensorDataDTO generateRandomSensorData() {
        // Generar datos realistas para un invernadero
        Double generalTemp = 20.0 + random.nextDouble() * 15; // 20-35°C
        Double generalHumidity = 40.0 + random.nextDouble() * 40; // 40-80%

        Double plantsTemp = generalTemp + (random.nextDouble() * 4 - 2); // ±2°C de la temperatura general
        Double plantsHumidity = generalHumidity + (random.nextDouble() * 20 - 10); // ±10% de la humedad general
        Integer plantsSoilMoisture = 30 + random.nextInt(70); // 30-100%

        Integer waterSoilMoisture = 40 + random.nextInt(60); // 40-100%
        Double waterPH = 6.0 + random.nextDouble() * 2.5; // 6.0-8.5
        Double waterTDS = 100.0 + random.nextDouble() * 900; // 100-1000 ppm

        return SensorDataDTO.builder()
                .generalTemperature(generalTemp)
                .generalHumidity(generalHumidity)
                .plantsTemperature(plantsTemp)
                .plantsHumidity(plantsHumidity)
                .plantsSoilMoisture(plantsSoilMoisture)
                .waterSoilMoisture(waterSoilMoisture)
                .waterPH(waterPH)
                .waterTDS(waterTDS)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Genera y guarda datos aleatorios incluyendo la evaluación de alertas
     */
    public SensorDataDTO generateAndSaveRandomData() {
        try {
            SensorDataDTO randomData = generateRandomSensorData();
            log.info("Generando datos aleatorios - Temp: {:.1f}°C, Hum: {:.1f}%",
                    randomData.getGeneralTemperature(), randomData.getGeneralHumidity());

            // Guardar los datos (esto automáticamente ejecutará alertaService.evaluarAlertas)
            SensorDataDTO savedData = sensorDataUseCase.save(randomData);

            log.info("Datos guardados y alertas evaluadas. Timestamp: {}",
                    savedData.getTimestamp() != null ? savedData.getTimestamp() : "N/A");

            return savedData;
        } catch (Exception e) {
            log.error("Error al generar y guardar datos aleatorios: {}", e.getMessage(), e);
            throw new RuntimeException("Error en generación de datos", e);
        }
    }
}