package com.agromatik.cloud.infrastructure.mysql.adapter;

import com.agromatik.cloud.application.port.out.SensorDataPort;
import com.agromatik.cloud.domain.model.SensorData;
import com.agromatik.cloud.infrastructure.mysql.repository.SpringDataSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SensorDataAdapter implements SensorDataPort {
    private final SpringDataSensorRepository repository;

    @Override
    public SensorData save(SensorData data) {
        return repository.save(data);
    }

    @Override
    public Page<SensorData> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<SensorData> findLatest() {
        return repository.findTopByOrderByTimestampDesc();
    }
}
