package com.agromatik.cloud.domain.service;
import com.agromatik.cloud.application.port.out.SensorHealthPort;
import com.agromatik.cloud.infrastructure.message.AlertService;
import com.agromatik.cloud.infrastructure.web.dto.SensorHealthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorHealthChecker {
    private final SensorHealthPort sensorHealthPort;
    private final AlertService alertService; // You'll need to implement this

    @Scheduled(fixedRate = 300000) // 5 minutes
    public void checkSensors() {
        List<SensorHealthDTO> healthStatuses = sensorHealthPort.checkAllSensorsHealth();

        healthStatuses.forEach(status -> {
            if (!status.isConnected()) {
                log.warn("Sensor {} is disconnected: {}", status.getSensorType(), status.getStatusMessage());
                alertService.sendAlert(
                        "Sensor Disconnected",
                        String.format("Sensor %s is disconnected. Last data: %s",
                                status.getSensorType(),
                                status.getLastDataReceived())
                );
            }
        });
    }
}
