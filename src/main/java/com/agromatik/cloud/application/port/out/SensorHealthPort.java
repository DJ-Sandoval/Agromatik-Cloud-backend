package com.agromatik.cloud.application.port.out;

import com.agromatik.cloud.infrastructure.web.dto.SensorHealthDTO;

import java.util.List;

public interface SensorHealthPort {
    List<SensorHealthDTO> checkAllSensorsHealth();
    void recordSensorActivity(String sensorType);
}
