package com.agromatik.cloud.infrastructure.mail;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailAlertService {
    private final JavaMailSender mailSender;
    private final EmailRateLimitConfig rateLimitConfig;

    private final ConcurrentHashMap<String, Queue<LocalDateTime>> emailSentTimestamps = new ConcurrentHashMap<>();

    public void enviarAlertaPorEmail(String destinatario, String asunto, String contenido) {
        if (!rateLimitConfig.isEnabled()) {
            log.info("Envío de emails desactivado por configuración");
            return;
        }

        if (!puedeEnviarEmail(destinatario)) {
            log.warn("Límite de emails alcanzado para: {}", destinatario);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destinatario);
            message.setSubject("[Agromatik] " + asunto);
            message.setText(contenido);
            message.setFrom("noreply@agromatik.com");

            mailSender.send(message);
            registrarEnvioEmail(destinatario);

            log.info("Alerta enviada por email a: {}", destinatario);
        } catch (Exception e) {
            log.error("Error enviando email a {}: {}", destinatario, e.getMessage());
        }
    }

    private boolean puedeEnviarEmail(String destinatario) {
        Queue<LocalDateTime> timestamps = emailSentTimestamps.getOrDefault(destinatario, new LinkedList<>());
        LocalDateTime unaHoraAtras = LocalDateTime.now().minusHours(1);

        // Limpiar timestamps antiguos
        while (!timestamps.isEmpty() && timestamps.peek().isBefore(unaHoraAtras)) {
            timestamps.poll();
        }

        return timestamps.size() < rateLimitConfig.getMaxEmailsPerHour();
    }

    private void registrarEnvioEmail(String destinatario) {
        Queue<LocalDateTime> timestamps = emailSentTimestamps.computeIfAbsent(destinatario, k -> new LinkedList<>());
        timestamps.offer(LocalDateTime.now());
    }

    // Limpiar registros antiguos cada hora
    @Scheduled(fixedRate = 3600000) // 1 hora
    public void limpiarRegistrosAntiguos() {
        LocalDateTime unaHoraAtras = LocalDateTime.now().minusHours(1);

        emailSentTimestamps.forEach((destinatario, timestamps) -> {
            timestamps.removeIf(timestamp -> timestamp.isBefore(unaHoraAtras));
            if (timestamps.isEmpty()) {
                emailSentTimestamps.remove(destinatario);
            }
        });

        log.debug("Registros de emails limpiados");
    }
}
