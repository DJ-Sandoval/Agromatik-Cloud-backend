package com.agromatik.cloud.infrastructure.ssh;

import com.agromatik.cloud.application.port.in.SshConnectionService;
import com.agromatik.cloud.domain.enums.SshConnectionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.jcraft.jsch.*;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Service
public class SshConnectorImpl implements SshConnectionService {

    private static final String SSH_USER = "agromatik";
    private static final String SSH_PASSWORD = "agromatik"; // Cambia por seguridad luego
    private static final String SSH_HOST = "raspberrypi";
    private static final int SSH_PORT = 22;
    private static final String REMOTE_COMMAND = "python3 arduino_bridge_gateway.py";

    @Override
    public SshConnectionStatus checkConnectionAndRunScript() {
        JSch jsch = new JSch();

        try {
            Session session = jsch.getSession(SSH_USER, SSH_HOST, SSH_PORT);
            session.setPassword(SSH_PASSWORD);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            log.info("Intentando conectar a {}@{}...", SSH_USER, SSH_HOST);
            session.connect(10_000); // Timeout 10 segundos

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(REMOTE_COMMAND);
            channel.setErrStream(System.err);

            InputStream input = channel.getInputStream();
            channel.connect();

            // Leer la salida del script
            byte[] buffer = new byte[1024];
            StringBuilder output = new StringBuilder();
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.append(new String(buffer, 0, bytesRead));
            }

            int exitStatus = channel.getExitStatus();
            channel.disconnect();
            session.disconnect();

            if (exitStatus == 0) {
                log.info("Script ejecutado correctamente. Output: {}", output);
                return SshConnectionStatus.CONNECTED;
            } else {
                log.warn("Script con errores. Exit code: {}", exitStatus);
                return SshConnectionStatus.DISCONNECTED;
            }

        } catch (Exception e) {
            log.error("Error al conectar por SSH: {}", e.getMessage());
            return SshConnectionStatus.DISCONNECTED;
        }
    }
}