package com.agromatik.cloud.infrastructure.web.dto;

import com.agromatik.cloud.domain.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private Long id;
    private String username;
    private Role role;
    private String token;
    private String message;
}
