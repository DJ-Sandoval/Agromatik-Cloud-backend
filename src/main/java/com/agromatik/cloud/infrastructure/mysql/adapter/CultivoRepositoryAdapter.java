package com.agromatik.cloud.infrastructure.mysql.adapter;

import com.agromatik.cloud.application.port.out.CultivoPort;
import com.agromatik.cloud.domain.model.Cultivo;
import com.agromatik.cloud.infrastructure.mysql.repository.SpringCultivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CultivoRepositoryAdapter implements CultivoPort {
    private final SpringCultivoRepository springCultivoRepository;

    @Override
    public Cultivo save(Cultivo cultivo) {
        return springCultivoRepository.save(cultivo);
    }

    @Override
    public Optional<Cultivo> findById(Long id) {
        return springCultivoRepository.findById(id);
    }

    @Override
    public Page<Cultivo> findAll(Pageable pageable) {
        return springCultivoRepository.findAll(pageable);
    }

    @Override
    public Page<Cultivo> findByUserId(Long userId, Pageable pageable) {
        return springCultivoRepository.findByUserId(userId, pageable);
    }

    @Override
    public void deleteById(Long id) {
        springCultivoRepository.deleteById(id);
    }
}
