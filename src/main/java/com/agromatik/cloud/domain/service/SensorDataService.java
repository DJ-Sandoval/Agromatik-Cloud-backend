package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.application.port.in.AlertaService;
import com.agromatik.cloud.application.port.in.SensorDataUseCase;
import com.agromatik.cloud.application.port.out.SensorDataPort;
import com.agromatik.cloud.domain.model.SensorData;
import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorDataService implements SensorDataUseCase {
    private final SensorDataPort port;
    private final AlertaService alertaService;

    @Override
    public SensorDataDTO save(SensorDataDTO dto) {
        log.info("Guardando datos del sensor - Temp: {}, Hum: {}",
                dto.getGeneralTemperature(), dto.getGeneralHumidity());

        SensorData entity = mapDtoToEntity(dto);
        log.info("Entidad mapeada: {}", entity);

        SensorData saved = port.save(entity);
        log.info("Entidad guardada en BD: {}", saved);

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
        // Usar los campos PLANOS del DTO en lugar de los objetos nested
        return SensorData.builder()
                .generalTemperature(dto.getGeneralTemperature())
                .generalHumidity(dto.getGeneralHumidity())
                .plantsTemperature(dto.getPlantsTemperature())
                .plantsHumidity(dto.getPlantsHumidity())
                .plantsSoilMoisture(dto.getPlantsSoilMoisture())
                .waterSoilMoisture(dto.getWaterSoilMoisture())
                .waterPH(dto.getWaterPH())
                .waterTDS(dto.getWaterTDS())
                .timestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now())
                .build();
    }

    private SensorDataDTO mapEntityToDto(SensorData entity) {
        // Crear objetos nested a partir de los campos planos de la entidad
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
                .timestamp(entity.getTimestamp())
                .build();
    }

}