package com.agromatik.cloud.application.port.in;

import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import com.agromatik.cloud.infrastructure.web.dto.SensorHealthDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SensorDataUseCase {
    SensorDataDTO save(SensorDataDTO dto);
    Page<SensorDataDTO> getAll(Pageable pageable);
    List<SensorHealthDTO> getSensorHealth();
    void checkSensorRanges(SensorDataDTO dto);
}
