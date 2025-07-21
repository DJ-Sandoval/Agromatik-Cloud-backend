package com.agromatik.cloud.application.port.out;

import com.agromatik.cloud.domain.model.SensorData;
import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SensorDataPort {
    SensorData save(SensorData data);
    Page<SensorData> findAll(Pageable pageable);
    Optional<SensorData> findLatest();
    List<SensorDataDTO> findBySensorIdAndDateRange(String sensorId, LocalDate start, LocalDate end);
}
