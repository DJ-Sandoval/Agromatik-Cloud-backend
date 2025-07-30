package com.agromatik.cloud.application.utils;
import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.stereotype.Component;

@Component
public class EmailRateLimiter {
    private final Queue<LocalDateTime> sendTimes = new ConcurrentLinkedQueue<>();
    private final int maxEmailsPerHour = 100;
    private final long windowInSeconds = 3600; // 1 hora

    public synchronized boolean trySendEmail() {
        LocalDateTime now = LocalDateTime.now();

        // Elimina tiempos de envío fuera de la ventana de 1 hora
        sendTimes.removeIf(time -> time.isBefore(now.minusSeconds(windowInSeconds)));

        // Verifica si se puede enviar un nuevo correo
        if (sendTimes.size() < maxEmailsPerHour) {
            sendTimes.add(now);
            return true;
        }
        return false;
    }
}
