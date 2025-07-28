package com.agromatik.cloud.application.port.in;

import com.agromatik.cloud.domain.model.Alerta;
import com.agromatik.cloud.domain.model.Recomendacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecomendacionService {
    Recomendacion generarRecomendacion(Alerta alerta);
    List<Recomendacion> obtenerRecomendacionesPorAlerta(Long alertaId);
    List<Recomendacion> obtenerRecomendacionesNoImplementadas();
    void marcarComoImplementada(Long recomendacionId);
    Page<Recomendacion> obtenerTodas(Pageable pageable);
}
