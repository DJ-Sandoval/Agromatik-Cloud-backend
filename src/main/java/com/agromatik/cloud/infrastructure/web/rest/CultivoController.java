package com.agromatik.cloud.infrastructure.web.rest;

import com.agromatik.cloud.application.port.in.CultivoService;
import com.agromatik.cloud.domain.model.Cultivo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agromatik/v1/cultivos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class CultivoController {
    private final CultivoService cultivoService;

    @PostMapping
    public ResponseEntity<Cultivo> registerCultivo(@RequestBody Cultivo cultivo) {
        return ResponseEntity.ok(cultivoService.registerCultivo(cultivo));
    }

    @PostMapping("/{cultivoId}/associate/{userId}")
    public ResponseEntity<Cultivo> associateCultivoToUser(@PathVariable Long cultivoId, @PathVariable Long userId) {
        return ResponseEntity.ok(cultivoService.associateCultivoToUser(cultivoId, userId));
    }

    @GetMapping
    public ResponseEntity<Page<Cultivo>> listCultivos(Pageable pageable) {
        return ResponseEntity.ok(cultivoService.listCultivos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cultivo> getCultivoById(@PathVariable Long id) {
        return ResponseEntity.ok(cultivoService.getCultivoById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivo(@PathVariable Long id) {
        cultivoService.deleteCultivo(id);
        return ResponseEntity.noContent().build();
    }
}
