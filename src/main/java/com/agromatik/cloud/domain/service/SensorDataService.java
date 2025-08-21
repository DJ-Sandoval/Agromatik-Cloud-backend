package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.application.port.in.AlertaService;
import com.agromatik.cloud.application.port.in.SensorDataUseCase;
import com.agromatik.cloud.application.port.out.SensorDataPort;
import com.agromatik.cloud.domain.model.SensorData;
import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SensorDataService implements SensorDataUseCase {
    private final SensorDataPort port;
    private final AlertaService alertaService;

    @Override
    public SensorDataDTO save(SensorDataDTO dto) {
        SensorData entity = mapDtoToEntity(dto);
        SensorData saved = port.save(entity);
        alertaService.evaluarAlertas(saved);
        return mapEntityToDto(saved);
    }

    @Override
    public Page<SensorDataDTO> getAll(Pageable pageable) {
        return port.findAll(pageable).map(this::mapEntityToDto);
    }

    @Override
    public Page<SensorDataDTO> getByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return port.findByTimestampBetween(startDate, endDate, pageable).map(this::mapEntityToDto);
    }

    private SensorData mapDtoToEntity(SensorDataDTO dto) {
        return SensorData.builder()
                .generalTemperature(dto.getGeneral() != null ? dto.getGeneral().getTemperature() : null)
                .generalHumidity(dto.getGeneral() != null ? dto.getGeneral().getHumidity() : null)
                .plantsTemperature(dto.getPlants() != null ? dto.getPlants().getTemperature() : null)
                .plantsHumidity(dto.getPlants() != null ? dto.getPlants().getHumidity() : null)
                .plantsSoilMoisture(dto.getPlants() != null ? dto.getPlants().getSoilMoisture() : null)
                .waterSoilMoisture(dto.getWater() != null ? dto.getWater().getSoilMoisture() : null)
                .waterPH(dto.getWater() != null ? dto.getWater().getPH() : null)
                .waterTDS(dto.getWater() != null ? dto.getWater().getTDS() : null)
                .build();
    }

    private SensorDataDTO mapEntityToDto(SensorData entity) {
        SensorDataDTO.GeneralData general = new SensorDataDTO.GeneralData();
        general.setTemperature(entity.getGeneralTemperature());
        general.setHumidity(entity.getGeneralHumidity());

        SensorDataDTO.PlantData plants = new SensorDataDTO.PlantData();
        plants.setTemperature(entity.getPlantsTemperature());
        plants.setHumidity(entity.getPlantsHumidity());
        plants.setSoilMoisture(entity.getPlantsSoilMoisture());

        SensorDataDTO.WaterData water = new SensorDataDTO.WaterData();
        water.setSoilMoisture(entity.getWaterSoilMoisture());
        water.setPH(entity.getWaterPH());
        water.setTDS(entity.getWaterTDS());

        return SensorDataDTO.builder()
                .general(general)
                .plants(plants)
                .water(water)
                .generalTemperature(entity.getGeneralTemperature())
                .generalHumidity(entity.getGeneralHumidity())
                .plantsTemperature(entity.getPlantsTemperature())
                .plantsHumidity(entity.getPlantsHumidity())
                .plantsSoilMoisture(entity.getPlantsSoilMoisture())
                .waterSoilMoisture(entity.getWaterSoilMoisture())
                .waterPH(entity.getWaterPH())
                .waterTDS(entity.getWaterTDS())
                .build();
    }

}