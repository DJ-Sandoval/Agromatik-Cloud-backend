package com.agromatik.cloud.infrastructure.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertNotificationService {
    private final EmailAlertService emailAlertService;
    private final List<String> destinatariosAlertas = new ArrayList<>();

    public void agregarDestinatario(String email) {
        if (!destinatariosAlertas.contains(email)) {
            destinatariosAlertas.add(email);
        }
    }

    public void removerDestinatario(String email) {
        destinatariosAlertas.remove(email);
    }

    public List<String> obtenerDestinatarios() {
        return new ArrayList<>(destinatariosAlertas);
    }

    public void notificarAlerta(String parametro, String descripcion, Double valorActual, String severidad) {
        String asunto = String.format("Alerta %s: %s", severidad, parametro);
        String contenido = construirContenidoEmail(parametro, descripcion, valorActual, severidad);

        for (String destinatario : destinatariosAlertas) {
            emailAlertService.enviarAlertaPorEmail(destinatario, asunto, contenido);
        }
    }

    private String construirContenidoEmail(String parametro, String descripcion, Double valorActual, String severidad) {
        return String.format(
                "Sistema de Alertas Agromatik\n\n" +
                        "Se ha detectado una alerta en el sistema:\n\n" +
                        "Parámetro: %s\n" +
                        "Descripción: %s\n" +
                        "Valor Actual: %s\n" +
                        "Severidad: %s\n" +
                        "Timestamp: %s\n\n" +
                        "Por favor, revise el sistema para tomar las acciones necesarias.\n\n" +
                        "Este es un mensaje automático, no responda a este email.",
                parametro, descripcion, valorActual, severidad, java.time.LocalDateTime.now()
        );
    }
}