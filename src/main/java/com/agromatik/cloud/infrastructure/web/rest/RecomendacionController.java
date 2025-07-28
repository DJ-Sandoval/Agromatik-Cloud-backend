package com.agromatik.cloud.infrastructure.web.rest;

import com.agromatik.cloud.application.port.in.RecomendacionService;
import com.agromatik.cloud.domain.model.Recomendacion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agromatik/recomendaciones")
@RequiredArgsConstructor
public class RecomendacionController {
    private final RecomendacionService recomendacionService;

    @GetMapping("/all")
    public ResponseEntity<List<Recomendacion>> obtenerTodas() {
        return ResponseEntity.ok(recomendacionService.obtenerTodas());
    }

    @GetMapping("/alerta/{alertaId}")
    public ResponseEntity<List<Recomendacion>> obtenerPorAlerta(@PathVariable Long alertaId) {
        return ResponseEntity.ok(recomendacionService.obtenerRecomendacionesPorAlerta(alertaId));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<Recomendacion>> obtenerPendientes() {
        return ResponseEntity.ok(recomendacionService.obtenerRecomendacionesNoImplementadas());
    }

    @PutMapping("/implementar/{id}")
    public ResponseEntity<Void> marcarComoImplementada(@PathVariable Long id) {
        recomendacionService.marcarComoImplementada(id);
        return ResponseEntity.noContent().build();
    }
}
