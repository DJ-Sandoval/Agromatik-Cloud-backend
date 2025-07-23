package com.agromatik.cloud.infrastructure.controller;

import com.agromatik.cloud.domain.model.SensorData;
import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;

public class SensorDataMapper {

    public static SensorData toEntity(SensorDataDTO dto) {
        return SensorData.builder()
                .plants(toEntityPlantData(dto.getPlants()))
                .water(toEntityWaterData(dto.getWater()))
                .timestamp(dto.getTimestamp())
                .build();
    }

    public static SensorDataDTO toDTO(SensorData entity) {
        return SensorDataDTO.builder()
                .plants(toDTOPlantData(entity.getPlants()))
                .water(toDTOWaterData(entity.getWater()))
                .generalTemperature(entity.getGeneral() != null ? entity.getGeneral().getTemperature() : null)
                .generalHumidity(entity.getGeneral() != null ? entity.getGeneral().getHumidity() : null)
                .plantsTemperature(entity.getPlants() != null ? entity.getPlants().getTemperature() : null)
                .plantsHumidity(entity.getPlants() != null ? entity.getPlants().getHumidity() : null)
                .plantsSoilMoisture(entity.getPlants() != null ? entity.getPlants().getSoilMoisture() : null)
                .waterSoilMoisture(entity.getWater() != null ? entity.getWater().getSoilMoisture() : null)
                .waterPH(entity.getWater() != null ? entity.getWater().getPH() : null)
                .waterTDS(entity.getWater() != null ? entity.getWater().getTDS() : null)
                .timestamp(entity.getTimestamp())
                .build();
    }

    private static SensorData.PlantData toEntityPlantData(SensorDataDTO.PlantData dto) {
        if (dto == null) return null;
        return SensorData.PlantData.builder()
                .temperature(dto.getTemperature())
                .humidity(dto.getHumidity())
                .soilMoisture(dto.getSoilMoisture())
                .build();
    }

    private static SensorData.WaterData toEntityWaterData(SensorDataDTO.WaterData dto) {
        if (dto == null) return null;
        return SensorData.WaterData.builder()
                .soilMoisture(dto.getSoilMoisture())
                .pH(dto.getPH())
                .TDS(dto.getTDS())
                .build();
    }

    private static SensorDataDTO.PlantData toDTOPlantData(SensorData.PlantData entity) {
        if (entity == null) return null;
        return SensorDataDTO.PlantData.builder()
                .temperature(entity.getTemperature())
                .humidity(entity.getHumidity())
                .soilMoisture(entity.getSoilMoisture())
                .build();
    }

    private static SensorDataDTO.WaterData toDTOWaterData(SensorData.WaterData entity) {
        if (entity == null) return null;
        return SensorDataDTO.WaterData.builder()
                .soilMoisture(entity.getSoilMoisture())
                .pH(entity.getPH())
                .TDS(entity.getTDS())
                .build();
    }
}
