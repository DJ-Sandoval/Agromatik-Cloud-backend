package com.agromatik.cloud.infrastructure.web.rest;

import com.agromatik.cloud.application.port.in.AlertaService;
import com.agromatik.cloud.domain.model.Alerta;
import com.agromatik.cloud.domain.model.SensorData;
import com.agromatik.cloud.domain.service.AlertaMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agromatik/alertas")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor
public class AlertaController {
    private final AlertaService alertaService;
    private final AlertaMonitorService alertaMonitorService;

    @GetMapping
    public ResponseEntity<Page<Alerta>> obtenerAlertas(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamaño) {
        return ResponseEntity.ok(alertaService.obtenerTodas(PageRequest.of(pagina, tamaño)));
    }

    @GetMapping("/no-leidas")
    public ResponseEntity<Page<Alerta>> obtenerAlertasNoLeidas(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamaño) {
        return ResponseEntity.ok(
                alertaService.obtenerPorEstadoLectura(false, PageRequest.of(pagina, tamaño)));
    }

    @GetMapping("/leidas")
    public ResponseEntity<Page<Alerta>> obtenerAlertasLeidas(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamaño) {
        return ResponseEntity.ok(
                alertaService.obtenerPorEstadoLectura(true, PageRequest.of(pagina, tamaño)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alerta> obtenerAlertaPorId(@PathVariable Long id) {
        return alertaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/marcar-leida")
    public ResponseEntity<Void> marcarAlertaComoLeida(@PathVariable Long id) {
        alertaService.marcarComoLeida(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/marcar-todas-leidas")
    public ResponseEntity<Void> marcarTodasAlertasComoLeidas() {
        alertaService.marcarTodasComoLeidas();
        return ResponseEntity.noContent().build();
    }

    // Endpoint para evaluación de alertas (si es necesario exponerlo)
    @PostMapping("/evaluar")
    public ResponseEntity<Void> evaluarDatosSensor(@RequestBody SensorData data) {
        alertaService.evaluarAlertas(data);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/nuevas")
    public ResponseEntity<List<Alerta>> obtenerAlertasRecientes() {
        return ResponseEntity.ok(alertaMonitorService.obtenerNuevasAlertasYLimpiar());
    }
}
