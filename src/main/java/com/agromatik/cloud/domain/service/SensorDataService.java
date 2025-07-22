package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.application.port.out.SensorHealthPort;
import com.agromatik.cloud.domain.enums.SensorType;
import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import com.agromatik.cloud.application.port.in.SensorDataUseCase;
import com.agromatik.cloud.application.port.out.SensorDataMongoPort;
import com.agromatik.cloud.domain.model.SensorDataMongo;
import com.agromatik.cloud.infrastructure.web.dto.SensorHealthDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SensorDataService implements SensorDataUseCase {
    private final SensorDataMongoPort mongoSensorDataPort;
    private final SensorHealthPort sensorHealthPort;
    private final SensorAlertService sensorAlertService;
    private final RuleEngineService ruleEngineService; // Nueva dependencia

    @Override
    public Mono<SensorDataDTO> save(SensorDataDTO dto) {
        SensorDataMongo entity = mapDtoToMongoEntity(dto);
        return mongoSensorDataPort.save(entity)
                .doOnSuccess(saved -> {
                    sensorHealthPort.recordSensorActivity("general");
                    sensorHealthPort.recordSensorActivity("plants");
                    sensorHealthPort.recordSensorActivity("water");
                    checkSensorRanges(dto);
                    ruleEngineService.evaluateRules(dto);
                })
                .map(this::mapMongoEntityToDto);
    }


    @Override
    public Mono<Page<SensorDataDTO>> getAll(Pageable pageable) {
        return mongoSensorDataPort.findAll(pageable)
                .map(page -> page.map(this::mapMongoEntityToDto));
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

    @Override
    public List<SensorHealthDTO> getSensorHealth() {
        return sensorHealthPort.checkAllSensorsHealth();
    }

    @Override
    public void checkSensorRanges(SensorDataDTO dto) {
        SensorType sensorType = SensorType.valueOf(dto.getType().toUpperCase());
        sensorAlertService.checkSensorValue(sensorType, dto.getValue(), dto.getSensorId());
    }

    @Override
    public Flux<SensorDataDTO> getSensorDataStream() {
        return Flux.interval(Duration.ofSeconds(1))
                .flatMap(seq -> mongoSensorDataPort.findLatest())
                .map(this::mapMongoEntityToDto);
    }
}
