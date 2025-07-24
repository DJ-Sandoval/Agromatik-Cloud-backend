package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.application.port.in.CultivoService;
import com.agromatik.cloud.application.port.out.CultivoRepository;
import com.agromatik.cloud.application.port.out.UserRepository;
import com.agromatik.cloud.domain.model.Cultivo;
import com.agromatik.cloud.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CultivoServiceImpl implements CultivoService {
    private final CultivoRepository cultivoRepository;
    private final UserRepository userRepository;

    @Override
    public Cultivo registerCultivo(Cultivo cultivo) {
        return cultivoRepository.save(cultivo);
    }

    @Override
    public Cultivo associateCultivoToUser(Long cultivoId, Long userId) {
        Cultivo cultivo = cultivoRepository.findById(cultivoId)
                .orElseThrow(() -> new RuntimeException("Cultivo not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        cultivo.setUser(user);
        return cultivoRepository.save(cultivo);
    }

    @Override
    public Page<Cultivo> listCultivos(Pageable pageable) {
        return cultivoRepository.findAll(pageable);
    }

    @Override
    public Cultivo getCultivoById(Long id) {
        return cultivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cultivo not found"));
    }

    @Override
    public void deleteCultivo(Long id) {
        if (!cultivoRepository.existsById(id)) {
            throw new RuntimeException("Cultivo not found");
        }
        cultivoRepository.deleteById(id);
    }
}
