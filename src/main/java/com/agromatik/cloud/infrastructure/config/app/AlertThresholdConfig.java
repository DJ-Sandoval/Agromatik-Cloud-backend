package com.agromatik.cloud.infrastructure.config.app;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AlertThresholdConfig {
    private Double disconnectedValue = -999.9;
    public Map<String, Threshold> getThresholds() {
        Map<String, Threshold> thresholds = new HashMap<>();
        thresholds.put("generalTemperature", new Threshold(10.0, 35.0));
        thresholds.put("generalHumidity", new Threshold(40.0, 90.0));
        thresholds.put("plantsTemperature", new Threshold(5.0, 30.0));
        thresholds.put("plantsHumidity", new Threshold(30.0, 80.0));
        thresholds.put("plantsSoilMoisture", new Threshold(30.0, 70.0));
        thresholds.put("waterPH", new Threshold(6.0, 8.0));
        thresholds.put("waterTDS", new Threshold(300.0, 1000.0)); // -999.9 = sensor off
        return thresholds;
    }

    public boolean isDisconnected(Double value) {
        return value != null && value.equals(disconnectedValue);
    }
    public record Threshold(Double min, Double max) {}
}
