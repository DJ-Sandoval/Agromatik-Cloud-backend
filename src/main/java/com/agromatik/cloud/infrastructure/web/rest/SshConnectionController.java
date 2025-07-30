package com.agromatik.cloud.infrastructure.web.rest;

import com.agromatik.cloud.application.port.in.SshConnectionService;
import com.agromatik.cloud.domain.enums.SshConnectionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/agromatik/ssh")
@RequiredArgsConstructor
public class SshConnectionController {

    private final SshConnectionService sshConnectionService;

    @GetMapping("/status")
    public String getSshConnectionStatus() {
        SshConnectionStatus status = sshConnectionService.checkConnectionAndRunScript();
        return "Raspberry Pi está " + status.name();
    }
}
