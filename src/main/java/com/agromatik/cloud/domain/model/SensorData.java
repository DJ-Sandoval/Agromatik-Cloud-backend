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

    // Flat values redundantes
    private Double generalTemperature;
    private Double generalHumidity;
    private Double plantsTemperature;
    private Double plantsHumidity;
    private Integer waterSoilMoisture;
    private Integer plantsSoilMoisture;
    private Double waterPH;
    private Double waterTDS;

    // Marca de tiempo de registro
    private LocalDateTime timestamp;


}
