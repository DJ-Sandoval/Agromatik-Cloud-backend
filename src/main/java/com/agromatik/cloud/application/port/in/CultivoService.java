package com.agromatik.cloud.application.port.in;

import com.agromatik.cloud.domain.model.Cultivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CultivoService {
    Cultivo registerCultivo(Cultivo cultivo);
    Cultivo associateCultivoToUser(Long cultivoId, Long userId);
    Page<Cultivo> listCultivos(Pageable pageable);
    Cultivo getCultivoById(Long id);
    void deleteCultivo(Long id);
}
