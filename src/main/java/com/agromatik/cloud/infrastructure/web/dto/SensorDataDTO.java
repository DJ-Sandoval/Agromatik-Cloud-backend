package com.agromatik.cloud.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorDataDTO {
    private GeneralData general;
    private PlantData plants;
    private WaterData water;

    private Double generalTemperature;
    private Double generalHumidity;
    private Double plantsTemperature;
    private Double plantsHumidity;
    private Integer waterSoilMoisture;
    private Integer plantsSoilMoisture;
    private Double waterPH;
    private Double waterTDS;
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
        @JsonProperty("pH")
        private Double pH;
        @JsonProperty("TDS")
        private Double TDS;
    }
}

