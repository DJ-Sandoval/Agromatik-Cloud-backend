package com.agromatik.cloud.infrastructure.mongo;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sensor_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorDataDocument {
    @Id
    private String id;
    private Double generalTemperature;
    private Double generalHumidity;
    private Double plantsTemperature;
    private Double plantsHumidity;
    private Integer waterSoilMoisture;
    private Integer plantsSoilMoisture;
    private Double waterPH;
    private Double waterTDS;
    private LocalDateTime timestamp;
}
