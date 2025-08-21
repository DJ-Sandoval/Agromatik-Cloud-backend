package com.agromatik.cloud.application.port.in;

import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorDataUseCase {
    SensorDataDTO save(SensorDataDTO dto);
    Page<SensorDataDTO> getAll(Pageable pageable);
    Page<SensorDataDTO> getByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}

