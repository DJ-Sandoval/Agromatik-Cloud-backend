package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.application.port.out.SensorHealthPort;
import com.agromatik.cloud.domain.enums.SensorType;
import com.agromatik.cloud.infrastructure.web.dto.SensorHealthDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SensorHealthService implements SensorHealthPort {
    private static final long SENSOR_TIMEOUT_MINUTES = 5;
    private final Map<String, LocalDateTime> lastActivityMap = new ConcurrentHashMap<>();

    @Override
    public List<SensorHealthDTO> checkAllSensorsHealth() {
        List<SensorHealthDTO> healthStatuses = new ArrayList<>();

        checkSensorHealth("general", healthStatuses);
        checkSensorHealth("plants", healthStatuses);
        checkSensorHealth("water", healthStatuses);

        return healthStatuses;
    }

    @Override
    public void recordSensorActivity(String sensorType) {
        lastActivityMap.put(sensorType, LocalDateTime.now());
    }

    private void checkSensorHealth(String sensorType, List<SensorHealthDTO> healthStatuses) {
        LocalDateTime lastActivity = lastActivityMap.get(sensorType);
        boolean isConnected = lastActivity != null &&
                ChronoUnit.MINUTES.between(lastActivity, LocalDateTime.now()) < SENSOR_TIMEOUT_MINUTES;

        String statusMessage = isConnected ?
                "Sensor active, last data received at " + lastActivity :
                "No data received for more than " + SENSOR_TIMEOUT_MINUTES + " minutes";

        healthStatuses.add(SensorHealthDTO.builder()
                .sensorType(SensorType.valueOf(sensorType.toUpperCase()))
                .connected(isConnected)
                .lastDataReceived(lastActivity)
                .statusMessage(statusMessage)
                .build());
    }
}
