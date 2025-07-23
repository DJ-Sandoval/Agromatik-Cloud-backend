package com.agromatik.cloud.infrastructure.mysql.repository;

import com.agromatik.cloud.domain.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDataJpaRepository extends JpaRepository<SensorData, String> {
}
