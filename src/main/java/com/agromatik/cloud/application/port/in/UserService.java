package com.agromatik.cloud.application.port.in;

import com.agromatik.cloud.infrastructure.web.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDto> listUsers(Pageable pageable);
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
}
