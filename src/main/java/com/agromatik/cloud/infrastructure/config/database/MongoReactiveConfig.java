package com.agromatik.cloud.infrastructure.config.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(
        basePackages = "com.agromatik.cloud.infrastructure.mongo.repository"
)
@EnableReactiveMongoAuditing
public class MongoReactiveConfig {
}
