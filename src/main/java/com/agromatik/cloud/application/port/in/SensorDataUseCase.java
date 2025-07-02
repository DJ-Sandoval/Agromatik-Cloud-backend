package com.agromatik.cloud.application.port.in;

import com.agromatik.cloud.application.dto.SensorDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SensorDataUseCase {
    SensorDataDTO save(SensorDataDTO dto);
    Page<SensorDataDTO> getAll(Pageable pageable);
}
