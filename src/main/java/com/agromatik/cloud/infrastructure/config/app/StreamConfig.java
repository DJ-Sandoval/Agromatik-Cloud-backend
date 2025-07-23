package com.agromatik.cloud.infrastructure.config.app;

import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class StreamConfig {

    @Bean
    public Sinks.Many<SensorDataDTO> sensorDataSink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }
}
