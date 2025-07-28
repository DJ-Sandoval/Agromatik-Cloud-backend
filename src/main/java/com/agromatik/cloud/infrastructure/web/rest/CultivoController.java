package com.agromatik.cloud.infrastructure.web.rest;

import com.agromatik.cloud.application.port.in.CultivoService;
import com.agromatik.cloud.domain.model.Cultivo;
import com.agromatik.cloud.infrastructure.web.dto.CultivoDTO;
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
    public ResponseEntity<Page<CultivoDTO>> listCultivos(Pageable pageable) {
        Page<CultivoDTO> result = cultivoService.listCultivos(pageable).map(cultivo ->
                CultivoDTO.builder()
                        .id(cultivo.getId())
                        .propietario(cultivo.getPropietario())
                        .ubicacionGPS(cultivo.getUbicacionGPS())
                        .nombreLote(cultivo.getNombreLote())
                        .region(cultivo.getRegion())
                        .tamanioHectarea(cultivo.getTamanioHectarea())
                        .cicloCultivo(cultivo.getCicloCultivo() != null ? cultivo.getCicloCultivo().name() : null)
                        .laboresCulturales(cultivo.getLaboresCulturales() != null ? cultivo.getLaboresCulturales().name() : null)
                        .metodoSiembra(cultivo.getMetodoSIembra() != null ? cultivo.getMetodoSIembra().name() : null)
                        .tipoCultivo(cultivo.getTipoCultivo() != null ? cultivo.getTipoCultivo().name() : null)
                        .userId(cultivo.getUser() != null ? cultivo.getUser().getId() : null)
                        .build()
        );
        return ResponseEntity.ok(result);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CultivoDTO> getCultivoById(@PathVariable Long id) {
        Cultivo cultivo = cultivoService.getCultivoById(id);
        CultivoDTO dto = CultivoDTO.builder()
                .id(cultivo.getId())
                .propietario(cultivo.getPropietario())
                .ubicacionGPS(cultivo.getUbicacionGPS())
                .nombreLote(cultivo.getNombreLote())
                .region(cultivo.getRegion())
                .tamanioHectarea(cultivo.getTamanioHectarea())
                .cicloCultivo(cultivo.getCicloCultivo() != null ? cultivo.getCicloCultivo().name() : null)
                .laboresCulturales(cultivo.getLaboresCulturales() != null ? cultivo.getLaboresCulturales().name() : null)
                .metodoSiembra(cultivo.getMetodoSIembra() != null ? cultivo.getMetodoSIembra().name() : null)
                .tipoCultivo(cultivo.getTipoCultivo() != null ? cultivo.getTipoCultivo().name() : null)
                .userId(cultivo.getUser() != null ? cultivo.getUser().getId() : null)
                .build();
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivo(@PathVariable Long id) {
        cultivoService.deleteCultivo(id);
        return ResponseEntity.noContent().build();
    }
}
