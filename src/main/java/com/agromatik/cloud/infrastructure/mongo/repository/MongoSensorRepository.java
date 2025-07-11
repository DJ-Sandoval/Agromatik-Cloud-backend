package com.agromatik.cloud.infrastructure.mongo.repository;

import com.agromatik.cloud.domain.model.SensorDataMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoSensorRepository extends MongoRepository<SensorDataMongo, String> {
    SensorDataMongo findTopByOrderByTimestampDesc();
}
