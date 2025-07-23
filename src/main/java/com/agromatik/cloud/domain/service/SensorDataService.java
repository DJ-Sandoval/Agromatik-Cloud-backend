package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.application.port.out.SensorDataRepositoryPort;
import com.agromatik.cloud.domain.model.SensorData;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorDataService {
    private final SensorDataRepositoryPort repository;

    public SensorData save(SensorData sensorData) {
        return repository.save(sensorData);
    }

    public Page<SensorData> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<SensorData> getLatest(int limit) {
        return repository.findLatest(limit);
    }
}