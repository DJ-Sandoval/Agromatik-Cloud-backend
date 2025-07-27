package com.agromatik.cloud.domain.model;

import com.agromatik.cloud.domain.enums.TipoRecomendacion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recomendaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recomendacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoRecomendacion tipo;

    private String parametro;
    private String descripcion;
    private String accionRecomendada;
    private String accionesAdicionales;
    private LocalDateTime timestamp;
    private boolean implementada;

    @ManyToOne
    @JoinColumn(name = "alerta_id")
    private Alerta alerta;
}
