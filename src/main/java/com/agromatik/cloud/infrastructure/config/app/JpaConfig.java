package com.agromatik.cloud.infrastructure.config.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.agromatik.cloud.application.port.out")
public class JpaConfig {
}
