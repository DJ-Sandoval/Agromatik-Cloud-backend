package com.agromatik.cloud.infrastructure.config.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration

@EnableJpaRepositories(basePackages = "com.agromatik.cloud.infrastructure.mysql.repository")
public class DatabaseConfig {
}
