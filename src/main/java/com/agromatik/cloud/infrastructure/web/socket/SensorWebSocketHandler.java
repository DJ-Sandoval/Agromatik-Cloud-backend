package com.agromatik.cloud.infrastructure.web.socket;

import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import com.agromatik.cloud.domain.model.SensorDataMongo;
import com.agromatik.cloud.infrastructure.mongo.repository.MongoSensorRepository;
import com.agromatik.cloud.infrastructure.redis.SensorCacheService;
import com.agromatik.cloud.infrastructure.web.dto.SensorHealthDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SensorWebSocketHandler implements WebSocketHandler {
    private final MongoSensorRepository mongoRepository;
    private final ObjectMapper objectMapper;
    private final SensorCacheService cacheService;


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<String> sensorDataFlux = Flux.interval(Duration.ofSeconds(1))
                .map(seq -> cacheService.getLatestSensorData()) // usa caché
                .filter(Objects::nonNull)
                .map(this::mapMongoEntityToDto)
                .map(dto -> {
                    try {
                        return objectMapper.writeValueAsString(dto);
                    } catch (Exception e) {
                        throw new RuntimeException("Error serializing SensorDataDTO", e);
                    }
                });

        return session.send(sensorDataFlux.map(session::textMessage))
                .and(session.receive().then());
    }

    private String createCombinedMessage(SensorDataMongo entity, List<SensorHealthDTO> healthStatus) {
        try {
            Map<String, Object> combined = new HashMap<>();
            combined.put("data", mapMongoEntityToDto(entity));
            combined.put("health", healthStatus);
            return objectMapper.writeValueAsString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing combined message", e);
        }
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