package com.agromatik.cloud.infrastructure.web.rest;

import com.agromatik.cloud.application.port.in.AlertaService;
import com.agromatik.cloud.application.port.in.SensorDataUseCase;
import com.agromatik.cloud.domain.model.SensorData;
import com.agromatik.cloud.domain.service.SensorDataGeneratorService;
import com.agromatik.cloud.domain.service.SensorDataService;
import com.agromatik.cloud.infrastructure.mysql.repository.SpringDataSensorRepository;
import com.agromatik.cloud.infrastructure.reports.pdf.PdfExportService;
import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/agromatik/telerimetry")
@RequiredArgsConstructor
public class SensorDataController {
    private final SensorDataUseCase useCase;
    private final SpringDataSensorRepository repository;
    private final AlertaService alertaService;
    private final SensorDataGeneratorService dataGeneratorService;
    private final PdfExportService pdfExportService;

    @PostMapping
    public SensorDataDTO save(@RequestBody SensorDataDTO dto) {
        return useCase.save(dto);
    }

    /*
    @GetMapping
    public Page<SensorDataDTO> getAll(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return useCase.getAll(PageRequest.of(page, size));
    }
    */


    @GetMapping
    public Page<SensorDataDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            return useCase.getByDateRange(startDateTime, endDateTime, PageRequest.of(page, size));
        }

        return useCase.getAll(PageRequest.of(page, size));
    }

    /*
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SensorDataDTO> streamSensorData() {
        return Flux.interval(Duration.ofSeconds(1))
                .flatMap(sequence -> Mono.fromCallable(() -> repository.findTopByOrderByTimestampDesc()))
                .map(optional -> optional.orElse(null))
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto);
    }
    */

    /*
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SensorDataDTO> streamSensorData() {
        return Flux.interval(Duration.ofSeconds(1))
                .flatMap(sequence -> Mono.fromCallable(() -> repository.findTopByOrderByTimestampDesc()))
                .map(optional -> optional.orElse(null))
                .filter(Objects::nonNull)
                .doOnNext(data -> {
                    // Evaluar alertas para cada dato recibido
                    //alertaService.evaluarAlertas(data);
                })
                .map(this::mapEntityToDto);
    }
    */

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SensorDataDTO> streamSensorData() {
        return Flux.interval(Duration.ofSeconds(1))
                .flatMap(sequence -> Mono.fromCallable(() -> repository.findTopByOrderByTimestampDesc())
                        .onErrorResume(e -> {
                            log.warn("Error fetching sensor data: {}", e.getMessage());
                            return Mono.empty();
                        }))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .filter(Objects::nonNull)
                .doOnNext(data -> {
                    try {
                        // Asegurar que el timestamp no sea nulo
                        if (data.getTimestamp() == null) {
                            data.setTimestamp(LocalDateTime.now());
                        }
                        // alertaService.evaluarAlertas(data);
                    } catch (Exception e) {
                        log.warn("Error evaluating alerts: {}", e.getMessage());
                    }
                });
    }

    private SensorDataDTO mapEntityToDto(SensorData entity) {
        if (entity == null) {
            return null;
        }

        try {
            SensorDataDTO.GeneralData general = new SensorDataDTO.GeneralData();
            general.setTemperature(entity.getGeneralTemperature());
            general.setHumidity(entity.getGeneralHumidity());

            SensorDataDTO.PlantData plants = new SensorDataDTO.PlantData();
            plants.setTemperature(entity.getPlantsTemperature());
            plants.setHumidity(entity.getPlantsHumidity());
            plants.setSoilMoisture(entity.getPlantsSoilMoisture());

            SensorDataDTO.WaterData water = new SensorDataDTO.WaterData();
            water.setSoilMoisture(entity.getWaterSoilMoisture());
            water.setPH(entity.getWaterPH());
            water.setTDS(entity.getWaterTDS());

            return SensorDataDTO.builder()
                    .general(general)
                    .plants(plants)
                    .water(water)
                    .generalTemperature(entity.getGeneralTemperature())
                    .generalHumidity(entity.getGeneralHumidity())
                    .plantsTemperature(entity.getPlantsTemperature())
                    .plantsHumidity(entity.getPlantsHumidity())
                    .plantsSoilMoisture(entity.getPlantsSoilMoisture())
                    .waterSoilMoisture(entity.getWaterSoilMoisture())
                    .waterPH(entity.getWaterPH())
                    .waterTDS(entity.getWaterTDS())
                    .timestamp(entity.getTimestamp()) // Usar el timestamp de la entidad, no now()
                    .build();
        } catch (Exception e) {
            log.error("Error mapping entity to DTO: {}", e.getMessage());
            return null;
        }
    }

    @GetMapping(path = "/generate-random", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SensorDataDTO> generateAndInsertRandomData() {
        return Flux.interval(Duration.ofSeconds(20))
                .map(sequence -> {
                    // Generar datos aleatorios
                    SensorDataDTO randomData = dataGeneratorService.generateRandomSensorData();
                    log.info("Generando datos aleatorios: Temp={}°C, Hum={}%",
                            randomData.getGeneralTemperature(), randomData.getGeneralHumidity());

                    // Insertar en la base de datos
                    try {
                        SensorDataDTO savedData = useCase.save(randomData);
                        log.info("Datos insertados correctamente. ID: {}",
                                savedData.getTimestamp() != null ? savedData.getTimestamp() : "N/A");
                        return savedData;
                    } catch (Exception e) {
                        log.error("Error insertando datos aleatorios: {}", e.getMessage());
                        // Devolver los datos generados aunque falle la inserción para el stream
                        return randomData;
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error en el stream de datos aleatorios: {}", e.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/export-pdf")
    public ResponseEntity<ByteArrayResource> exportToPdf(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "false") boolean download) throws IOException {

        // Obtener datos según el rango de fechas
        List<SensorData> sensorDataList;
        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            sensorDataList = repository.findByTimestampBetween(startDateTime, endDateTime);
        } else {
            sensorDataList = repository.findTop100ByOrderByTimestampDesc(); // Límite para evitar PDFs muy grandes
        }

        // Convertir a DTO
        List<SensorDataDTO> sensorDataDTOs = sensorDataList.stream()
                .map(this::mapEntityToDto)
                .filter(Objects::nonNull)
                .toList();

        // Generar PDF
        byte[] pdfBytes = pdfExportService.generateSensorDataPdf(sensorDataDTOs);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        // Configurar headers según si es descarga o visualización
        HttpHeaders headers = new HttpHeaders();
        String filename = "reporte_lecturas_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";

        if (download) {
            // Descarga directa
            headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        } else {
            // Visualización en navegador
            headers.setContentDisposition(ContentDisposition.inline().filename(filename).build());
        }

        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(pdfBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
