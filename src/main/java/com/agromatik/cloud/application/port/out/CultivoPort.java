package com.agromatik.cloud.application.port.out;

import com.agromatik.cloud.domain.model.Cultivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CultivoPort {
    Cultivo save(Cultivo cultivo);
    Optional<Cultivo> findById(Long id);
    Page<Cultivo> findAll(Pageable pageable);
    Page<Cultivo> findByUserId(Long userId, Pageable pageable);
    void deleteById(Long id);
}
