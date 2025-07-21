package com.agromatik.cloud.infrastructure.web.dto;

import com.agromatik.cloud.domain.enums.CropType;
import com.agromatik.cloud.domain.enums.GrowthStage;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsRequestDTO {
    private String sensorId;
    private CropType cropType;
    private GrowthStage growthStage;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double waterThreshold; // Umbral para estrés hídrico
}
