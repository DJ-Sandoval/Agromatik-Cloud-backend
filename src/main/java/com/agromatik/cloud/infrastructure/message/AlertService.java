package com.agromatik.cloud.infrastructure.message;

import com.agromatik.cloud.application.port.out.AlertPort;
import com.agromatik.cloud.domain.enums.Severity;
import com.agromatik.cloud.infrastructure.mongo.AlertDocument;
import com.agromatik.cloud.infrastructure.web.dto.AlertDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertPort alertPort;

    public void sendAlert(String title, String message) {
        sendAlert(title, message, Severity.WARNING.name());
    }

    public void sendAlert(String title, String message, String severity) {
        AlertDocument alert = AlertDocument.builder()
                .title(title)
                .message(message)
                .timestamp(LocalDateTime.now())
                .acknowledged(false)
                .severity(Severity.valueOf(severity.toUpperCase()))
                .build();

        alertPort.save(alert);
        log.warn("New alert created: {} - {}", title, message);
    }

    public List<AlertDTO> getAllAlerts() {
        return alertPort.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<AlertDTO> getUnacknowledgedAlerts() {
        return alertPort.findUnacknowledged().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public AlertDTO acknowledgeAlert(String alertId) {
        return mapToDto(alertPort.acknowledge(alertId));
    }

    private AlertDTO mapToDto(AlertDocument alert) {
        return AlertDTO.builder()
                .id(alert.getId())
                .title(alert.getTitle())
                .message(alert.getMessage())
                .timestamp(alert.getTimestamp())
                .acknowledged(alert.isAcknowledged())
                .severity(alert.getSeverity())
                .build();
    }
}
