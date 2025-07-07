package com.agromatik.cloud.application.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorDataDTO {
    private GeneralData general;
    private PlantsData plants;
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
    public static class PlantsData {
        private Double temperature;
        private Double humidity;
        private Double soilMoisture;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WaterData {
        private Double soilMoisture;
        private Double pH;
        private Double TDS;
    }
}

