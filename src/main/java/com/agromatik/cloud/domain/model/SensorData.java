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
    private String id;

    @Embedded
    private GeneralData general;

    @Embedded
    private PlantData plants;

    @Embedded
    private WaterData water;

    private LocalDateTime timestamp;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GeneralData {
        private Double temperature;
        private Double humidity;
    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlantData {
        private Double temperature;
        private Double humidity;
        private Integer soilMoisture;
    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WaterData {
        private Integer soilMoisture;
        private Double pH;
        @Column(name = "tds")
        private Double TDS;
    }
}