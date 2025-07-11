package com.agromatik.cloud.application.port.out;


import com.agromatik.cloud.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    void deleteById(Long id);
}
