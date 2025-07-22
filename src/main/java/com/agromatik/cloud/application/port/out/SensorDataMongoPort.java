package com.agromatik.cloud.application.port.out;

import com.agromatik.cloud.domain.model.SensorDataMongo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface SensorDataMongoPort {
    Mono<SensorDataMongo> save(SensorDataMongo entity);
    Mono<Page<SensorDataMongo>> findAll(Pageable pageable);
    Mono<SensorDataMongo> findLatest();
}
