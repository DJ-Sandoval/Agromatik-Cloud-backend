package com.agromatik.cloud.domain.model;

import com.agromatik.cloud.domain.enums.CicloCultivo;
import com.agromatik.cloud.domain.enums.LaboresCulturales;
import com.agromatik.cloud.domain.enums.MetodoSiembra;
import com.agromatik.cloud.domain.enums.TipoCultivo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cultivo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cultivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String propietario;
    private String ubicacionGPS;
    private String nombreLote;
    private String region;
    private Double tamanioHectarea;

    @Enumerated(EnumType.STRING)
    private CicloCultivo cicloCultivo;

    @Enumerated(EnumType.STRING)
    private LaboresCulturales laboresCulturales;

    @Enumerated(EnumType.STRING)
    private MetodoSiembra metodoSIembra;

    @Enumerated(EnumType.STRING)
    private TipoCultivo tipoCultivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
