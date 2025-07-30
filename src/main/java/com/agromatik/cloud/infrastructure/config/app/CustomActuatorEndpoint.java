package com.agromatik.cloud.infrastructure.config.app;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "customEndpoint")  // La URL será /actuator/customEndpoint
public class CustomActuatorEndpoint {

    @ReadOperation  // Para solicitudes GET
    public Map<String, Object> customData() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "running");
        data.put("appName", "AgromatikCloud");
        data.put("version", "1.25.0");
        data.put("customMetric", 42);

        // Puedes agregar lógica personalizada aquí
        // Ej: Recuperar stats de la base de datos, etc.

        return data;
    }
}
