package com.agromatik.cloud.infrastructure.config.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "AgromatikCloud API",
                version = "3.0",
                description = "Documentación de la API REST de AgromatikCloud")
)
public class OpenAPIConfig {}
