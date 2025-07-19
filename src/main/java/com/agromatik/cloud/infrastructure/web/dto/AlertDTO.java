package com.agromatik.cloud.infrastructure.web.dto;

import com.agromatik.cloud.domain.enums.Severity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertDTO {
    private String id;
    private String title;
    private String message;
    private LocalDateTime timestamp;
    private boolean acknowledged;
    private Severity severity; // "INFO", "WARNING", "CRITICAL"
}
