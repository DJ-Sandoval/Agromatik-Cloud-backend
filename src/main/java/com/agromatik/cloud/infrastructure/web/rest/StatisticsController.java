package com.agromatik.cloud.infrastructure.web.rest;

import com.agromatik.cloud.application.port.in.StatisticsUseCase;
import com.agromatik.cloud.infrastructure.web.dto.StatisticsRequestDTO;
import com.agromatik.cloud.infrastructure.web.dto.StatisticsResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/agromatik/statistics")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsUseCase statisticsUseCase;

    @PostMapping
    public StatisticsResponseDTO getStatistics(@RequestBody StatisticsRequestDTO request) {
        return statisticsUseCase.calculateStatistics(request);
    }
}
