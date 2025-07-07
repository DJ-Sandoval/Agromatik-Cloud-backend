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

        if (dto.getGeneral() != null) {
            entity.setGeneralTemperature(dto.getGeneral().getTemperature());
            entity.setGeneralHumidity(dto.getGeneral().getHumidity());
        }

        if (dto.getPlants() != null) {
            entity.setPlantsTemperature(dto.getPlants().getTemperature());
            entity.setPlantsHumidity(dto.getPlants().getHumidity());
            entity.setPlantsSoilMoisture(dto.getPlants().getSoilMoisture());
        }

        if (dto.getWater() != null) {
            entity.setWaterSoilMoisture(dto.getWater().getSoilMoisture());
            entity.setWaterPH(dto.getWater().getPH());
            entity.setWaterTDS1(dto.getWater().getTDS());
        }

        // Puedes calcular promedios aquí si quieres, o dejarlos nulos por ahora
        entity.setTimestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now());

        // Guardar entidad
        SensorData saved = port.save(entity);

        // Mapear entidad guardada a DTO para respuesta (convertir plano a anidado)
        SensorDataDTO responseDto = mapEntityToDto(saved);

        return responseDto;
    }

    @Override
    public Page<SensorDataDTO> getAll(Pageable pageable) {
        return port.findAll(pageable)
                .map(entity -> {
                    SensorDataDTO.GeneralData general = SensorDataDTO.GeneralData.builder()
                            .temperature(entity.getGeneralTemperature())
                            .humidity(entity.getGeneralHumidity())
                            .build();

                    SensorDataDTO.PlantsData plants = SensorDataDTO.PlantsData.builder()
                            .temperature(entity.getPlantsTemperature())
                            .humidity(entity.getPlantsHumidity())
                            .soilMoisture(entity.getPlantsSoilMoisture())
                            .build();

                    SensorDataDTO.WaterData water = SensorDataDTO.WaterData.builder()
                            .soilMoisture(entity.getWaterSoilMoisture())
                            .pH(entity.getWaterPH())
                            .TDS(entity.getWaterTDS1())
                            .build();

                    return SensorDataDTO.builder()
                            .general(general)
                            .plants(plants)
                            .water(water)
                            .timestamp(entity.getTimestamp())
                            .build();
                });
    }


    private SensorDataDTO mapEntityToDto(SensorData entity) {
        SensorDataDTO.GeneralData general = SensorDataDTO.GeneralData.builder()
                .temperature(entity.getGeneralTemperature())
                .humidity(entity.getGeneralHumidity())
                .build();

        SensorDataDTO.PlantsData plants = SensorDataDTO.PlantsData.builder()
                .temperature(entity.getPlantsTemperature())
                .humidity(entity.getPlantsHumidity())
                .soilMoisture(entity.getPlantsSoilMoisture())
                .build();

        SensorDataDTO.WaterData water = SensorDataDTO.WaterData.builder()
                .soilMoisture(entity.getWaterSoilMoisture())
                .pH(entity.getWaterPH())
                .TDS(entity.getWaterTDS1())
                .build();

        return SensorDataDTO.builder()
                .general(general)
                .plants(plants)
                .water(water)
                .timestamp(entity.getTimestamp())
                .build();
    }
}
