package com.agromatik.cloud.infrastructure.web.dto;

import com.agromatik.cloud.domain.enums.SensorType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorHealthDTO {
    private SensorType sensorType;  // "general", "plants", "water"
    private boolean connected;
    private LocalDateTime lastDataReceived;
    private String statusMessage;
}
