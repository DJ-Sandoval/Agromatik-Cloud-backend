package com.agromatik.cloud.application.port.out;

import com.agromatik.cloud.domain.model.Recomendacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RecomendacionPort {
    Recomendacion guardar(Recomendacion recomendacion);
    List<Recomendacion> buscarPorAlertaId(Long alertaId);
    List<Recomendacion> buscarNoImplementadas();
    void actualizarComoImplementada(Long id);
    Optional<Recomendacion> buscarPorId(Long id);
    Page<Recomendacion> buscarTodas(Pageable pageable);
}
