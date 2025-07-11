package com.agromatik.cloud.infrastructure.web;

import com.agromatik.cloud.application.dto.SensorDataDTO;
import com.agromatik.cloud.application.port.in.SensorDataUseCase;
import com.agromatik.cloud.domain.model.SensorData;
import com.agromatik.cloud.domain.model.SensorDataMongo;
import com.agromatik.cloud.infrastructure.mongo.repository.MongoSensorRepository;
import com.agromatik.cloud.infrastructure.mysql.repository.SpringDataSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RequestMapping("/api/v1/agromatik/telerimetry")
@RequiredArgsConstructor
public class SensorController {
    private final SensorDataUseCase useCase;
    private final MongoSensorRepository mongoRepository; // Cambiado a repositorio de MongoDB

    @PostMapping
    public SensorDataDTO save(@RequestBody SensorDataDTO dto) {
        return useCase.save(dto);
    }

    @GetMapping
    public Page<SensorDataDTO> getAll(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return useCase.getAll(PageRequest.of(page, size));
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SensorDataDTO> streamSensorData() {
        return Flux.interval(Duration.ofSeconds(1))
                .flatMap(sequence -> Mono.fromCallable(() -> mongoRepository.findTopByOrderByTimestampDesc()))
                .filter(Objects::nonNull)
                .map(this::mapMongoEntityToDto);
    }

    private SensorDataDTO mapMongoEntityToDto(SensorDataMongo entity) {
        SensorDataDTO.GeneralData general = new SensorDataDTO.GeneralData();
        general.setTemperature(entity.getGeneral().getTemperature());
        general.setHumidity(entity.getGeneral().getHumidity());

        SensorDataDTO.PlantData plants = new SensorDataDTO.PlantData();
        plants.setTemperature(entity.getPlants().getTemperature());
        plants.setHumidity(entity.getPlants().getHumidity());
        plants.setSoilMoisture(entity.getPlants().getSoilMoisture());

        SensorDataDTO.WaterData water = new SensorDataDTO.WaterData();
        water.setSoilMoisture(entity.getWater().getSoilMoisture());
        water.setPH(entity.getWater().getPH());
        water.setTDS(entity.getWater().getTDS());

        return SensorDataDTO.builder()
                .general(general)
                .plants(plants)
                .water(water)
                .timestamp(entity.getTimestamp())
                .build();
    }
}
