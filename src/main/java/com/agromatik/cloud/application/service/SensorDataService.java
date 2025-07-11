package com.agromatik.cloud.application.service;

import com.agromatik.cloud.application.dto.SensorDataDTO;
import com.agromatik.cloud.application.port.in.SensorDataUseCase;
import com.agromatik.cloud.application.port.out.SensorDataMongoPort;
import com.agromatik.cloud.application.port.out.SensorDataPort;
import com.agromatik.cloud.domain.model.SensorData;
import com.agromatik.cloud.domain.model.SensorDataMongo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SensorDataService implements SensorDataUseCase {
    private final SensorDataMongoPort mongoSensorDataPort;

    @Override
    public SensorDataDTO save(SensorDataDTO dto) {
        SensorDataMongo entity = mapDtoToMongoEntity(dto);
        SensorDataMongo saved = mongoSensorDataPort.save(entity);
        return mapMongoEntityToDto(saved);
    }

    @Override
    public Page<SensorDataDTO> getAll(Pageable pageable) {
        return mongoSensorDataPort.findAll(pageable).map(this::mapMongoEntityToDto);
    }

    private SensorDataMongo mapDtoToMongoEntity(SensorDataDTO dto) {
        SensorDataMongo.GeneralData general = new SensorDataMongo.GeneralData();
        general.setTemperature(dto.getGeneralTemperature());
        general.setHumidity(dto.getGeneralHumidity());

        SensorDataMongo.PlantData plants = new SensorDataMongo.PlantData();
        plants.setTemperature(dto.getPlantsTemperature());
        plants.setHumidity(dto.getPlantsHumidity());
        plants.setSoilMoisture(dto.getPlantsSoilMoisture());

        SensorDataMongo.WaterData water = new SensorDataMongo.WaterData();
        water.setSoilMoisture(dto.getWaterSoilMoisture());
        water.setPH(dto.getWaterPH());
        water.setTDS(dto.getWaterTDS());

        return SensorDataMongo.builder()
                .general(general)
                .plants(plants)
                .water(water)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private SensorDataDTO mapMongoEntityToDto(SensorDataMongo entity) {
        return SensorDataDTO.builder()
                .generalTemperature(entity.getGeneral().getTemperature())
                .generalHumidity(entity.getGeneral().getHumidity())
                .plantsTemperature(entity.getPlants().getTemperature())
                .plantsHumidity(entity.getPlants().getHumidity())
                .plantsSoilMoisture(entity.getPlants().getSoilMoisture())
                .waterSoilMoisture(entity.getWater().getSoilMoisture())
                .waterPH(entity.getWater().getPH())
                .waterTDS(entity.getWater().getTDS())
                .timestamp(entity.getTimestamp())
                .build();
    }

}
