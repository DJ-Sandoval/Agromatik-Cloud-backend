package com.agromatik.cloud.domain.model;

import com.agromatik.cloud.domain.enums.Severity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alertas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String parametro; // Ej: plantsSoilMoisture
    private String descripcion; // "Humedad del suelo baja"
    private Double valorActual;
    private Double umbralMin;
    private Double umbralMax;

    @Enumerated(EnumType.STRING)
    private Severity severidad;

    private LocalDateTime timestamp;

    private boolean leida;
}
