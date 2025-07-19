package com.agromatik.cloud.infrastructure.web.rest;

import com.agromatik.cloud.infrastructure.message.AlertService;
import com.agromatik.cloud.infrastructure.web.dto.AlertDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agromatik/alerts")
@RequiredArgsConstructor
public class AlertController {
    private final AlertService alertService;

    @GetMapping
    public List<AlertDTO> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    @GetMapping("/unacknowledged")
    public List<AlertDTO> getUnacknowledgedAlerts() {
        return alertService.getUnacknowledgedAlerts();
    }

    @PostMapping("/{alertId}/acknowledge")
    public AlertDTO acknowledgeAlert(@PathVariable String alertId) {
        return alertService.acknowledgeAlert(alertId);
    }
}
