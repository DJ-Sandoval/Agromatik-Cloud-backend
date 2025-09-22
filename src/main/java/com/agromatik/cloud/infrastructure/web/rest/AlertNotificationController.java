package com.agromatik.cloud.infrastructure.web.rest;

import com.agromatik.cloud.infrastructure.mail.AlertNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agromatik/alertas/notificaciones")
@RequiredArgsConstructor
public class AlertNotificationController {
    private final AlertNotificationService notificationService;

    @PostMapping("/destinatarios")
    public ResponseEntity<Void> agregarDestinatario(@RequestParam String email) {
        notificationService.agregarDestinatario(email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/destinatarios")
    public ResponseEntity<Void> removerDestinatario(@RequestParam String email) {
        notificationService.removerDestinatario(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/destinatarios")
    public ResponseEntity<List<String>> obtenerDestinatarios() {
        return ResponseEntity.ok(notificationService.obtenerDestinatarios());
    }
}
