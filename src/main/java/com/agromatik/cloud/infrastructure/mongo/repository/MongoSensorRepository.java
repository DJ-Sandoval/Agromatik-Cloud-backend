package com.agromatik.cloud.infrastructure.mongo.repository;

import com.agromatik.cloud.domain.model.SensorDataMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MongoSensorRepository extends ReactiveMongoRepository<SensorDataMongo, String> {
    Mono<SensorDataMongo> findTopByOrderByTimestampDesc();
}
