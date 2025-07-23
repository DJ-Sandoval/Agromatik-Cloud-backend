package com.agromatik.cloud.infrastructure.mysql.adapter;

import com.agromatik.cloud.application.port.out.SensorDataRepositoryPort;
import com.agromatik.cloud.domain.model.SensorData;
import com.agromatik.cloud.infrastructure.mysql.repository.SensorDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SensorDataRepositoryAdapter implements SensorDataRepositoryPort {
    private final SensorDataJpaRepository jpaRepository;

    @Override
    public SensorData save(SensorData sensorData) {
        return jpaRepository.save(sensorData);
    }

    @Override
    public Page<SensorData> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public List<SensorData> findLatest(int limit) {
        return jpaRepository.findAll(Pageable.ofSize(limit)).getContent();
    }
}
