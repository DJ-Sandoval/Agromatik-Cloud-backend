package com.agromatik.cloud.application.port.out;

import com.agromatik.cloud.domain.model.SensorData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SensorDataPort {
    SensorData save(SensorData data);
    Page<SensorData> findAll(Pageable pageable);
    Optional<SensorData> findLatest();
}
