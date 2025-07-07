package com.agromatik.cloud.infrastructure.web;

import com.agromatik.cloud.application.dto.SensorDataDTO;
import com.agromatik.cloud.application.port.in.SensorDataUseCase;
import com.agromatik.cloud.domain.model.SensorData;
import com.agromatik.cloud.infrastructure.persistence.repository.SpringDataSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;
import org.springframework.beans.BeanUtils;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RequestMapping("/api/v1/agromatik")
@RequiredArgsConstructor
public class SensorController {
    private final SensorDataUseCase useCase;
    private final SpringDataSensorRepository repository;

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
                .flatMap(sequence -> Mono.fromCallable(() -> repository.findTopByOrderByTimestampDesc()))
                .map(optional -> optional.orElse(null))
                .filter(entity -> entity != null)
                .map(entity -> mapEntityToDto(entity));
    }

    private SensorDataDTO mapEntityToDto(SensorData entity) {
        SensorDataDTO.GeneralData general = SensorDataDTO.GeneralData.builder()
                .temperature(entity.getGeneralTemperature())
                .humidity(entity.getGeneralHumidity())
                .build();

        SensorDataDTO.PlantsData plants = SensorDataDTO.PlantsData.builder()
                .temperature(entity.getPlantsTemperature())
                .humidity(entity.getPlantsHumidity())
                .soilMoisture(entity.getPlantsSoilMoisture())
                .build();

        SensorDataDTO.WaterData water = SensorDataDTO.WaterData.builder()
                .soilMoisture(entity.getWaterSoilMoisture())
                .pH(entity.getWaterPH())
                .TDS(entity.getWaterTDS1())
                .build();

        return SensorDataDTO.builder()
                .general(general)
                .plants(plants)
                .water(water)
                .timestamp(entity.getTimestamp())
                .build();
    }


}
