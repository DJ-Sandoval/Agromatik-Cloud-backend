package com.agromatik.cloud.infrastructure.web.rest;

import com.agromatik.cloud.application.port.in.AuthService;
import com.agromatik.cloud.infrastructure.web.dto.LoginRequest;
import com.agromatik.cloud.infrastructure.web.dto.LoginResponse;
import com.agromatik.cloud.domain.service.JwtService;
import com.agromatik.cloud.domain.model.User;
import com.agromatik.cloud.infrastructure.web.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(authService.register(userDto));
    }
}
