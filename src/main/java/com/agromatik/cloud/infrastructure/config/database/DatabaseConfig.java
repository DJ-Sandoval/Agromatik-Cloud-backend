package com.agromatik.cloud.infrastructure.config.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.agromatik.cloud.infrastructure.mongo.repository")
@EnableJpaRepositories(basePackages = "com.agromatik.cloud.infrastructure.mysql.repository")
public class DatabaseConfig {
}
