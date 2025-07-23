package com.agromatik.cloud.application.port.in;

import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SensorDataUseCase {
    Mono<SensorDataDTO> save(SensorDataDTO dto);
    Mono<Page<SensorDataDTO>> getAll(Pageable pageable);
    void checkSensorRanges(SensorDataDTO dto);
    Flux<SensorDataDTO> getSensorDataStream();
}
