package com.agromatik.cloud.infrastructure.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoEmailService {

    private final AlertNotificationService alertNotificationService;
    private final EmailAlertService emailAlertService;

    @Value("${spring.application.email.destinatario-automatico:}")
    private String destinatarioAutomatico;

    @PostConstruct
    public void init() {
        // Agregar destinatario automáticamente al iniciar la aplicación
        if (destinatarioAutomatico != null && !destinatarioAutomatico.trim().isEmpty()) {
            alertNotificationService.agregarDestinatario(destinatarioAutomatico);
            System.out.println("✅ Destinatario automático configurado: " + destinatarioAutomatico);
        }
    }

    public void enviarAlertaAutomatica(String parametro, String descripcion, Double valorActual, String severidad) {
        if (destinatarioAutomatico != null && !destinatarioAutomatico.trim().isEmpty()) {
            String asunto = String.format("Alerta Automática %s: %s", severidad, parametro);
            String contenido = construirContenidoEmail(parametro, descripcion, valorActual, severidad);

            emailAlertService.enviarAlertaPorEmail(destinatarioAutomatico, asunto, contenido);
        }
    }

    private String construirContenidoEmail(String parametro, String descripcion, Double valorActual, String severidad) {
        return String.format(
                "Sistema de Alertas Automáticas Agromatik\n\n" +
                        "Se ha detectado una alerta automática:\n\n" +
                        "Parámetro: %s\n" +
                        "Descripción: %s\n" +
                        "Valor Actual: %s\n" +
                        "Severidad: %s\n" +
                        "Timestamp: %s\n\n" +
                        "Este es un mensaje automático del sistema.\n\n" +
                        "Saludos,\nSistema Agromatik Cloud",
                parametro, descripcion, valorActual, severidad, java.time.LocalDateTime.now()
        );
    }

    public String getDestinatarioAutomatico() {
        return destinatarioAutomatico;
    }
}
