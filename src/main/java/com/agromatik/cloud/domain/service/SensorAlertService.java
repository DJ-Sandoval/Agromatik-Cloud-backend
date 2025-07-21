package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.domain.enums.SensorType;
import com.agromatik.cloud.domain.enums.Severity;
import com.agromatik.cloud.infrastructure.message.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorAlertService {
    private final AlertService alertService;

    public void checkSensorValue(SensorType sensorType, double value, String sensorId) {
        if (sensorType.isOutOfRange(value)) {
            String title = String.format("Sensor %s out of range", sensorType.name());
            String message = String.format("Sensor %s (ID: %s) value %.2f is outside acceptable range (%.2f - %.2f)",
                    sensorType.name(), sensorId, value, sensorType.getMinValue(), sensorType.getMaxValue());

            Severity severity = determineSeverity(sensorType, value);
            alertService.sendAlert(title, message, severity.name());
        }
    }

    private Severity determineSeverity(SensorType sensorType, double value) {
        // Lógica para determinar la severidad basada en qué tan fuera de rango está
        double range = sensorType.getMaxValue() - sensorType.getMinValue();
        double deviation = Math.max(
                Math.abs(value - sensorType.getMinValue()),
                Math.abs(value - sensorType.getMaxValue())
        );

        if (deviation > range * 0.5) {
            return Severity.CRITICAL;
        } else if (deviation > range * 0.2) {
            return Severity.WARNING;
        } else {
            return Severity.INFO;
        }
    }
}
