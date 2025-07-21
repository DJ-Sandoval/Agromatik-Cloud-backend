package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.application.port.in.StatisticsUseCase;
import com.agromatik.cloud.application.port.out.SensorDataPort;
import com.agromatik.cloud.domain.enums.CropType;
import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import com.agromatik.cloud.infrastructure.web.dto.StatisticsRequestDTO;
import com.agromatik.cloud.infrastructure.web.dto.StatisticsResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService implements StatisticsUseCase {
    private final SensorDataPort sensorDataPort;

    private static final Map<CropType, Double> BASE_TEMPERATURES = Map.of(
            CropType.MAIZE, 10.0,
            CropType.WHEAT, 0.0,
            CropType.SOYBEAN, 10.0,
            CropType.RICE, 10.0
    );

    @Override
    public StatisticsResponseDTO calculateStatistics(StatisticsRequestDTO request) {
        List<SensorDataDTO> data = sensorDataPort.findBySensorIdAndDateRange(
                request.getSensorId(),
                request.getStartDate(),
                request.getEndDate()
        );

        if (data.isEmpty()) {
            return StatisticsResponseDTO.builder().build();
        }

        // Separar datos de temperatura y humedad
        List<Double> temperatures = data.stream()
                .filter(d -> "TEMPERATURE".equalsIgnoreCase(d.getType()))
                .map(SensorDataDTO::getValue)
                .collect(Collectors.toList());

        List<Double> humidities = data.stream()
                .filter(d -> "HUMIDITY".equalsIgnoreCase(d.getType()))
                .map(SensorDataDTO::getValue)
                .collect(Collectors.toList());

        // Cálculos básicos
        Double avgTemp = calculateAverage(temperatures);
        Double avgHumidity = calculateAverage(humidities);

        // Cálculos avanzados
        int stressHours = calculateWaterStressHours(humidities, request.getWaterThreshold());
        double gdd = calculateGrowingDegreeDays(temperatures, request.getCropType());
        double irrigationEff = calculateIrrigationEfficiency(humidities);

        // Agrupar por día para promedios diarios
        Map<String, Double> dailyAverages = data.stream()
                .collect(Collectors.groupingBy(
                        d -> d.getTimestamp().toLocalDate().toString(),
                        Collectors.averagingDouble(SensorDataDTO::getValue)
                ));
        return StatisticsResponseDTO.builder()
                .averageTemperature(avgTemp)
                .averageHumidity(avgHumidity)
                .maxTemperature(Collections.max(temperatures))
                .minTemperature(Collections.min(temperatures))
                .maxHumidity(Collections.max(humidities))
                .minHumidity(Collections.min(humidities))
                .waterStressHours(stressHours)
                .growingDegreeDays(gdd)
                .irrigationEfficiency(irrigationEff)
                .dailyAverages(dailyAverages)
                .build();
    }


    private Double calculateAverage(List<Double> values) {
        return values.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    private int calculateWaterStressHours(List<Double> humidities, Double threshold) {
        if (threshold == null) return 0;
        return (int) humidities.stream()
                .filter(h -> h < threshold)
                .count();
    }

    private double calculateGrowingDegreeDays(List<Double> temperatures, CropType cropType) {
        double baseTemp = BASE_TEMPERATURES.getOrDefault(cropType, 10.0);
        return temperatures.stream()
                .mapToDouble(t -> Math.max(0, t - baseTemp))
                .sum();
    }

    private double calculateIrrigationEfficiency(List<Double> humidities) {
        if (humidities.size() < 2) return 0.0;

        double optimal = 70.0; // Humedad óptima teórica
        double sumSquaredErrors = humidities.stream()
                .mapToDouble(h -> Math.pow(h - optimal, 2))
                .sum();

        double mse = sumSquaredErrors / humidities.size();
        return 100.0 - (mse / optimal * 100);
    }
}
