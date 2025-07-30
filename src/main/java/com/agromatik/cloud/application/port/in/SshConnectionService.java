package com.agromatik.cloud.application.port.in;

import com.agromatik.cloud.domain.enums.SshConnectionStatus;

public interface SshConnectionService {
    SshConnectionStatus checkConnectionAndRunScript();
}
