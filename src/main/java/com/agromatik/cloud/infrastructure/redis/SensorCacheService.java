package com.agromatik.cloud.infrastructure.redis;

import com.agromatik.cloud.domain.model.SensorDataMongo;
import com.agromatik.cloud.infrastructure.mongo.repository.MongoSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorCacheService {

    private final MongoSensorRepository mongoRepository;
    /*
    @Cacheable(value = "latestSensorData")
    public SensorDataMongo getLatestSensorData() {
        return mongoRepository.findTopByOrderByTimestampDesc();
    }
    */

}