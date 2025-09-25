package com.agromatik.cloud.infrastructure.reports.pdf;

import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfExportService {

    private final ResourceLoader resourceLoader;

    public byte[] generateSensorDataPdf(List<SensorDataDTO> sensorDataList) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Configuración inicial
                float margin = 50;
                float yPosition = PDRectangle.A4.getHeight() - margin;
                float lineHeight = 14;
                float tableTop = yPosition - 100;

                // Cargar y agregar el icono
                try {
                    Resource resource = resourceLoader.getResource("classpath:static/icon.png");
                    if (resource.exists()) {
                        PDImageXObject icon = PDImageXObject.createFromFile(resource.getFile().getAbsolutePath(), document);
                        contentStream.drawImage(icon, margin, yPosition - 30, 30, 30);
                    }
                } catch (Exception e) {
                    log.warn("No se pudo cargar el icono: {}", e.getMessage());
                }

                // Título del reporte
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin + 35, yPosition - 20);
                contentStream.showText("Reporte de lecturas AgromatikCloud");
                contentStream.endText();

                // Fecha actual
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.beginText();
                String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                contentStream.newLineAtOffset(margin, yPosition - 50);
                contentStream.showText("Fecha de generación: " + fechaActual);
                contentStream.endText();

                // Tabla de datos
                drawSensorDataTable(contentStream, sensorDataList, margin, tableTop, lineHeight);

            } catch (Exception e) {
                log.error("Error generando PDF: {}", e.getMessage());
                throw e;
            }

            // Guardar en byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    private void drawSensorDataTable(PDPageContentStream contentStream,
                                     List<SensorDataDTO> sensorDataList,
                                     float margin, float tableTop, float lineHeight) throws IOException {

        // Encabezado de la tabla con color azul medio
        float[] columnWidths = {80, 70, 70, 70, 70, 70, 70, 70};
        String[] headers = {"Fecha/Hora", "Temp. General", "Hum. General", "Temp. Plantas",
                "Hum. Plantas", "Humedad Suelo", "pH Agua", "TDS Agua"};

        // Dibujar fondo del encabezado
        contentStream.setNonStrokingColor(new Color(100, 149, 237)); // Azul medio
        contentStream.addRect(margin, tableTop - lineHeight,
                getTotalWidth(columnWidths), lineHeight);
        contentStream.fill();

        // Texto del encabezado
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);

        float xPosition = margin;
        for (int i = 0; i < headers.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(xPosition + 2, tableTop - lineHeight + 4);
            contentStream.showText(headers[i]);
            contentStream.endText();
            xPosition += columnWidths[i];
        }

        // Datos de la tabla
        contentStream.setFont(PDType1Font.HELVETICA, 9);
        float currentY = tableTop - lineHeight * 2;

        for (SensorDataDTO data : sensorDataList) {
            if (currentY < margin + lineHeight * 2) {
                break; // Evitar desbordamiento de página
            }

            String[] rowData = {
                    data.getTimestamp() != null ?
                            data.getTimestamp().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")) : "N/A",
                    formatDouble(data.getGeneralTemperature()),
                    formatDouble(data.getGeneralHumidity()),
                    formatDouble(data.getPlantsTemperature()),
                    formatDouble(data.getPlantsHumidity()),
                    data.getPlantsSoilMoisture() != null ? data.getPlantsSoilMoisture() + "%" : "N/A",
                    formatDouble(data.getWaterPH()),
                    formatDouble(data.getWaterTDS())
            };

            xPosition = margin;
            for (int i = 0; i < rowData.length; i++) {
                contentStream.beginText();
                contentStream.newLineAtOffset(xPosition + 2, currentY);
                contentStream.showText(rowData[i]);
                contentStream.endText();
                xPosition += columnWidths[i];
            }

            // Línea separadora
            contentStream.moveTo(margin, currentY - 2);
            contentStream.lineTo(margin + getTotalWidth(columnWidths), currentY - 2);
            contentStream.stroke();

            currentY -= lineHeight;
        }
    }

    private float getTotalWidth(float[] columnWidths) {
        float total = 0;
        for (float width : columnWidths) {
            total += width;
        }
        return total;
    }

    private String formatDouble(Double value) {
        return value != null ? String.format("%.2f", value) : "N/A";
    }
}