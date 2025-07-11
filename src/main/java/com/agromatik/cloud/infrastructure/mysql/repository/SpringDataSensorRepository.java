package com.agromatik.cloud.infrastructure.mysql.repository;

import com.agromatik.cloud.domain.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SpringDataSensorRepository extends JpaRepository<SensorData, Long> {
    Optional<SensorData> findTopByOrderByTimestampDesc();
}
