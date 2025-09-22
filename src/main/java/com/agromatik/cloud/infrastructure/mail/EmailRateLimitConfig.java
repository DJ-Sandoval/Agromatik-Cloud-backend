package com.agromatik.cloud.infrastructure.mail;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.email")
public class EmailRateLimitConfig {
    private int maxEmailsPerHour = 5;
    private boolean enabled = true;
}