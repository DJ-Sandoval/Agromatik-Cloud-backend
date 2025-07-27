package com.agromatik.cloud.application.port.out;

import com.agromatik.cloud.domain.model.Alerta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AlertaPort {
    Alerta guardar(Alerta alerta);
    Page<Alerta> buscarTodas(Pageable pageable);
    Page<Alerta> buscarPorLeida(boolean leida, Pageable pageable);
    Optional<Alerta> buscarPorId(Long id);
    void actualizarComoLeida(Long id);
    void actualizarTodasComoLeidas();
}
