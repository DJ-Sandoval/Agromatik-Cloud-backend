package com.agromatik.cloud.application.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorDataDTO {
    private Double generalTemperature;
    private Double plantsTemperature;
    private Double generalHumidity;
    private Double plantsHumidity;
    private Double waterSoilMoisture;
    private Double plantsSoilMoisture;
    private Double waterTDS1;
    private Double waterTDS2;
    private Double waterPH;
    private Double temperatureAvg;
    private Double humidityAvg;
    private Double soilMoistureAvg;
    private Double tdsAvg;
    private LocalDateTime timestamp;
}
