package com.agromatik.cloud.infrastructure.mysql.repository;

import com.agromatik.cloud.domain.model.SensorData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataSensorRepository extends JpaRepository<SensorData, Long> {
    Optional<SensorData> findTopByOrderByTimestampDesc();
    Page<SensorData> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    List<SensorData> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<SensorData> findTop100ByOrderByTimestampDesc();
}
