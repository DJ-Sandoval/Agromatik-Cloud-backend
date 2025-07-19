package com.agromatik.cloud.infrastructure.mongo;

import com.agromatik.cloud.domain.enums.Severity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertDocument {
    @Id
    private String id;
    private String title;
    private String message;
    private LocalDateTime timestamp;
    private boolean acknowledged;
    @Enumerated(EnumType.STRING)
    private Severity severity;
}