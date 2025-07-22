package com.agromatik.cloud.infrastructure.web.rest;

import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import com.agromatik.cloud.application.port.in.SensorDataUseCase;
import com.agromatik.cloud.infrastructure.web.dto.SensorHealthDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RequestMapping("/api/v1/agromatik/telerimetry")
@RequiredArgsConstructor
public class SensorController {
    private final SensorDataUseCase useCase;

    @PostMapping
    public Mono<SensorDataDTO> save(@RequestBody SensorDataDTO dto) {
        return useCase.save(dto);
    }

    @GetMapping
    public Mono<Page<SensorDataDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return useCase.getAll(PageRequest.of(page, size));
    }


    @GetMapping("/health")
    public List<SensorHealthDTO> getSensorHealth() {
        return useCase.getSensorHealth();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SensorDataDTO> streamSensorData() {
        return useCase.getSensorDataStream();
    }
}
