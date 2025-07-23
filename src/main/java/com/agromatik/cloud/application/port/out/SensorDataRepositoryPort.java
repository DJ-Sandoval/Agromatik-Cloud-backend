package com.agromatik.cloud.application.port.out;

import com.agromatik.cloud.domain.model.SensorData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SensorDataRepositoryPort {
    SensorData save(SensorData sensorData);
    Page<SensorData> findAll(Pageable pageable);
    List<SensorData> findLatest(int limit);
}
