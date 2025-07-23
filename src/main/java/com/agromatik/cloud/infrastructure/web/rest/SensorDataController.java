package com.agromatik.cloud.infrastructure.web.rest;

import com.agromatik.cloud.domain.model.SensorData;
import com.agromatik.cloud.domain.service.SensorDataService;
import com.agromatik.cloud.infrastructure.controller.SensorDataMapper;
import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import lombok.RequiredArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/agromatik/telerimetry")
@RequiredArgsConstructor
public class SensorDataController {

    private final SensorDataService service;
    private final Sinks.Many<SensorDataDTO> sink;

    @PostMapping
    public SensorDataDTO save(@RequestBody SensorDataDTO dto) {
        dto.setTimestamp(LocalDateTime.now());
        SensorData entity = SensorDataMapper.toEntity(dto);
        SensorData saved = service.save(entity);
        SensorDataDTO result = SensorDataMapper.toDTO(saved);
        sink.tryEmitNext(result);
        return result;
    }

    @GetMapping
    public Page<SensorDataDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.getAll(PageRequest.of(page, size))
                .map(SensorDataMapper::toDTO);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream() {
        return sink.asFlux()
                .map(dto -> {
                    try {
                        return "data:" + new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(dto) + "\n\n";
                    } catch (Exception e) {
                        return "";
                    }
                });
    }
}
