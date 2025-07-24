package com.agromatik.cloud.application.port.in;

import com.agromatik.cloud.infrastructure.web.dto.LoginRequest;
import com.agromatik.cloud.infrastructure.web.dto.LoginResponse;
import com.agromatik.cloud.infrastructure.web.dto.UserDto;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    UserDto register(UserDto userDto);
}
