package com.agromatik.cloud.application.port.in;

import com.agromatik.cloud.infrastructure.web.dto.StatisticsRequestDTO;
import com.agromatik.cloud.infrastructure.web.dto.StatisticsResponseDTO;

public interface StatisticsUseCase {
    StatisticsResponseDTO calculateStatistics(StatisticsRequestDTO request);
}
