package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.application.port.in.UserService;
import com.agromatik.cloud.infrastructure.mysql.repository.SpringUserRepository;
import com.agromatik.cloud.infrastructure.web.dto.CultivoDTO;
import com.agromatik.cloud.infrastructure.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import com.agromatik.cloud.domain.model.User;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SpringUserRepository userRepository;

    @Override
    public Page<UserDto> listUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(user -> {
            List<CultivoDTO> cultivos = user.getCultivos() != null
                    ? user.getCultivos().stream().map(c -> CultivoDTO
                    .builder()
                    .id(c.getId())
                    .propietario(c.getPropietario())
                    .ubicacionGPS(c.getUbicacionGPS())
                    .nombreLote(c.getNombreLote())
                    .region(c.getRegion())
                    .tamanioHectarea(c.getTamanioHectarea())
                    .cicloCultivo(c.getCicloCultivo() != null ? c.getCicloCultivo().name() : null)
                    .laboresCulturales(c.getLaboresCulturales() != null ? c.getLaboresCulturales().name() : null)
                    .metodoSiembra(c.getMetodoSIembra() != null ? c.getMetodoSIembra().name() : null)
                    .tipoCultivo(c.getTipoCultivo() != null ? c.getTipoCultivo().name() : null)
                    .userId(c.getUser() != null ? c.getUser().getId() : null)
                    .build()
            ).toList()
                    : new ArrayList<>();

            return UserDto.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .role(user.getRole())
                    .cultivos(cultivos)
                    .build();
        });
    }


    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CultivoDTO> cultivos = user.getCultivos() != null
                ? user.getCultivos().stream().map(c -> CultivoDTO.builder()
                .id(c.getId())
                .propietario(c.getPropietario())
                .ubicacionGPS(c.getUbicacionGPS())
                .nombreLote(c.getNombreLote())
                .region(c.getRegion())
                .tamanioHectarea(c.getTamanioHectarea())
                .cicloCultivo(c.getCicloCultivo() != null ? c.getCicloCultivo().name() : null)
                .laboresCulturales(c.getLaboresCulturales() != null ? c.getLaboresCulturales().name() : null)
                .metodoSiembra(c.getMetodoSIembra() != null ? c.getMetodoSIembra().name() : null)
                .tipoCultivo(c.getTipoCultivo() != null ? c.getTipoCultivo().name() : null)
                .userId(c.getUser() != null ? c.getUser().getId() : null)
                .build()
        ).toList()
                : new ArrayList<>();

        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .cultivos(cultivos)
                .build();
    }


    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
            user.setPassword(userDto.getPassword()); // Password encoding handled in controller
        }
        user.setRole(userDto.getRole());
        userRepository.save(user);
        return userDto;
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}


