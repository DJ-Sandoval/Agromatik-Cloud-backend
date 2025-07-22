package com.agromatik.cloud.infrastructure.mongo.adapter;

import com.agromatik.cloud.application.port.out.SensorDataMongoPort;
import com.agromatik.cloud.application.port.out.SensorDataPort;
import com.agromatik.cloud.domain.model.SensorDataMongo;
import com.agromatik.cloud.infrastructure.mongo.repository.MongoSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MongoSensorDataAdapter implements SensorDataMongoPort {
    private final MongoSensorRepository repository;

    @Override
    public Mono<SensorDataMongo> save(SensorDataMongo data) {
        return repository.save(data);
    }

    @Override
    public Mono<Page<SensorDataMongo>> findAll(Pageable pageable) {
        return repository.findAll()
                .collectList()
                .zipWith(repository.count())
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    @Override
    public Mono<SensorDataMongo> findLatest() {
        return repository.findTopByOrderByTimestampDesc();
    }


}
