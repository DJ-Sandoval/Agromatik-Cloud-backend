package com.agromatik.cloud.domain.enums;

import lombok.Getter;

@Getter
public enum SensorType {
    GENERAL(Double.MIN_VALUE, Double.MAX_VALUE),
    TEMPERATURE(-10.0, 40.0),  // Ejemplo: rango para temperatura
    PLANTS(0.0, 100.0);        // Ejemplo: humedad de plantas (0-100%)

    private final double minValue;
    private final double maxValue;

    SensorType(double minValue, double maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public boolean isOutOfRange(double value) {
        return value < minValue || value > maxValue;
    }
}
