package com.agromatik.cloud.application.port.out;

import com.agromatik.cloud.domain.model.SensorDataMongo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SensorDataMongoPort {
    SensorDataMongo save(SensorDataMongo data);
    Page<SensorDataMongo> findAll(Pageable pageable);
    Optional<SensorDataMongo> findLatest();
}
