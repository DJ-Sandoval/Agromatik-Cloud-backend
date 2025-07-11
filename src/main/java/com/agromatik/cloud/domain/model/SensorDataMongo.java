package com.agromatik.cloud.domain.model;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sensor_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorDataMongo {
    @Id
    private String id;

    private GeneralData general;
    private PlantData plants;
    private WaterData water;
    private LocalDateTime timestamp;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GeneralData {
        private Double temperature;
        private Double humidity;
    }

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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WaterData {
        private Integer soilMoisture;
        private Double pH;
        private Double TDS;
    }
}
