package com.agromatik.cloud.infrastructure.mysql.adapter;

import com.agromatik.cloud.application.port.out.UserRepositoryPort;
import com.agromatik.cloud.domain.model.User;
import com.agromatik.cloud.infrastructure.mysql.repository.SpringUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final SpringUserRepository springUserRepository;

    @Override
    public User save(User user) {
        return springUserRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return springUserRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return springUserRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springUserRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return springUserRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        springUserRepository.deleteById(id);
    }
}
