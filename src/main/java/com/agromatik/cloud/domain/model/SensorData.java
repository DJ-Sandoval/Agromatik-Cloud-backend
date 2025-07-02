package com.agromatik.cloud.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
