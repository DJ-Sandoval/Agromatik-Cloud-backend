package com.agromatik.cloud.infrastructure.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CultivoDTO {
    private Long id;
    private String propietario;
    private String ubicacionGPS;
    private String nombreLote;
    private String region;
    private Double tamanioHectarea;
    private String cicloCultivo;
    private String laboresCulturales;
    private String metodoSiembra;
    private String tipoCultivo;
    private Long userId;
}

