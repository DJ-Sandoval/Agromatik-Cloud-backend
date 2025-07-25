package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.application.port.in.CultivoService;
import com.agromatik.cloud.application.port.out.CultivoPort;
import com.agromatik.cloud.application.port.out.UserRepositoryPort;
import com.agromatik.cloud.domain.model.Cultivo;
import com.agromatik.cloud.domain.model.User;
import com.agromatik.cloud.infrastructure.mysql.repository.SpringCultivoRepository;
import com.agromatik.cloud.infrastructure.mysql.repository.SpringUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CultivoServiceImpl implements CultivoService {
    private final CultivoPort cultivoPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Cultivo registerCultivo(Cultivo cultivo) {
        return cultivoPort.save(cultivo);
    }

    @Override
    public Cultivo associateCultivoToUser(Long cultivoId, Long userId) {
        Cultivo cultivo = cultivoPort.findById(cultivoId)
                .orElseThrow(() -> new RuntimeException("Cultivo not found"));
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        cultivo.setUser(user);
        return cultivoPort.save(cultivo);
    }

    @Override
    public Page<Cultivo> listCultivos(Pageable pageable) {
        return cultivoPort.findAll(pageable);
    }

    @Override
    public Cultivo getCultivoById(Long id) {
        return cultivoPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Cultivo not found"));
    }

    @Override
    public void deleteCultivo(Long id) {
        if (cultivoPort.findById(id).isEmpty()) {
            throw new RuntimeException("Cultivo not found");
        }
        cultivoPort.deleteById(id);
    }
}