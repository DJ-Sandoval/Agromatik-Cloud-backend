package com.agromatik.cloud.infrastructure.web.rest;

import com.agromatik.cloud.application.port.in.AlertaService;
import com.agromatik.cloud.application.port.in.SensorDataUseCase;
import com.agromatik.cloud.domain.model.SensorData;
import com.agromatik.cloud.domain.service.SensorDataService;
import com.agromatik.cloud.infrastructure.mysql.repository.SpringDataSensorRepository;
import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/agromatik/telerimetry")
@RequiredArgsConstructor
public class SensorDataController {
    private final SensorDataUseCase useCase;
    private final SpringDataSensorRepository repository;
    private final AlertaService alertaService;

    @PostMapping
    public SensorDataDTO save(@RequestBody SensorDataDTO dto) {
        return useCase.save(dto);
    }

    @GetMapping
    public Page<SensorDataDTO> getAll(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return useCase.getAll(PageRequest.of(page, size));
    }

    /*
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SensorDataDTO> streamSensorData() {
        return Flux.interval(Duration.ofSeconds(1))
                .flatMap(sequence -> Mono.fromCallable(() -> repository.findTopByOrderByTimestampDesc()))
                .map(optional -> optional.orElse(null))
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto);
    }
    */

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SensorDataDTO> streamSensorData() {
        return Flux.interval(Duration.ofSeconds(1))
                .flatMap(sequence -> Mono.fromCallable(() -> repository.findTopByOrderByTimestampDesc()))
                .map(optional -> optional.orElse(null))
                .filter(Objects::nonNull)
                .doOnNext(data -> {
                    // Evaluar alertas para cada dato recibido
                    //alertaService.evaluarAlertas(data);
                })
                .map(this::mapEntityToDto);
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
                .timestamp(LocalDateTime.now())
                .build();
    }

}
