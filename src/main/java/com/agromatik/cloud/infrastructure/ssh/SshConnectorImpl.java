package com.agromatik.cloud.infrastructure.ssh;

import com.agromatik.cloud.application.port.in.SshConnectionService;
import com.agromatik.cloud.domain.enums.SshConnectionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SshConnectorImpl implements SshConnectionService {

    private static final String SSH_USER = "agromatik";
    private static final String SSH_HOST = "raspberrypi";
    private static final String SCRIPT = "python3 arduino_bridge_gateway.py";

    @Override
    public SshConnectionStatus checkConnectionAndRunScript() {
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "ssh",
                    SSH_USER + "@" + SSH_HOST,
                    SCRIPT
            );
            builder.redirectErrorStream(true);
            Process process = builder.start();

            // Timeout de 10 segundos
            boolean finished = process.waitFor(10, TimeUnit.SECONDS);
            if (!finished) {
                process.destroy();
                log.warn("Timeout al intentar conectar por SSH");
                return SshConnectionStatus.DISCONNECTED;
            }

            int exitCode = process.exitValue();
            if (exitCode == 0) {
                log.info("Conectado y script ejecutado con éxito");
                return SshConnectionStatus.CONNECTED;
            } else {
                log.error("Error al ejecutar script por SSH. Exit code: {}", exitCode);
                return SshConnectionStatus.DISCONNECTED;
            }

        } catch (IOException | InterruptedException e) {
            log.error("Error en conexión SSH", e);
            return SshConnectionStatus.DISCONNECTED;
        }
    }
}
