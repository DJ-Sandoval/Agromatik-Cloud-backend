package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.application.port.in.AuthService;
import com.agromatik.cloud.application.port.out.UserRepositoryPort;
import com.agromatik.cloud.domain.model.User;
import com.agromatik.cloud.infrastructure.web.dto.LoginRequest;
import com.agromatik.cloud.infrastructure.web.dto.LoginResponse;
import com.agromatik.cloud.infrastructure.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepositoryPort userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String token = jwtService.generateToken(user);
            Long cultivoId = user.getCultivos().isEmpty() ? null : user.getCultivos().get(0).getId();
            return LoginResponse.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .cultivoId(cultivoId)
                    .token(token)
                    .message("Login successful")
                    .build();
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }


    @Override
    public UserDto register(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(userDto.getRole())
                .build();

        userRepository.save(user);
        return userDto;
    }
}
