package com.agromatik.cloud.infrastructure.web.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsResponseDTO {
    private Double averageTemperature;
    private Double averageHumidity;
    private Double maxTemperature;
    private Double minTemperature;
    private Double maxHumidity;
    private Double minHumidity;
    private Integer waterStressHours;
    private Double growingDegreeDays;
    private Double irrigationEfficiency;
    private Map<String, Double> dailyAverages; // Promedios diarios
}
