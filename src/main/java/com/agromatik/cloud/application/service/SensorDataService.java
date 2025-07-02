package com.agromatik.cloud.application.service;

import com.agromatik.cloud.application.dto.SensorDataDTO;
import com.agromatik.cloud.application.port.in.SensorDataUseCase;
import com.agromatik.cloud.application.port.out.SensorDataPort;
import com.agromatik.cloud.domain.model.SensorData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SensorDataService implements SensorDataUseCase {
    private final SensorDataPort port;

    @Override
    public SensorDataDTO save(SensorDataDTO dto) {
        SensorData entity = new SensorData();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getTimestamp() == null)
            entity.setTimestamp(LocalDateTime.now());

        SensorData saved = port.save(entity);
        SensorDataDTO result = new SensorDataDTO();
        BeanUtils.copyProperties(saved, result);
        return result;
    }

    @Override
    public Page<SensorDataDTO> getAll(Pageable pageable) {
        return port.findAll(pageable).map(entity -> {
            SensorDataDTO dto = new SensorDataDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
    }
}
