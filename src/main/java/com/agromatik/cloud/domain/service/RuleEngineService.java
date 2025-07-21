package com.agromatik.cloud.domain.service;

import com.agromatik.cloud.infrastructure.message.AlertService;
import com.agromatik.cloud.infrastructure.web.dto.SensorDataDTO;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

@Service
public class RuleEngineService {
    private final KieContainer kieContainer;
    private final AlertService alertService;

    public RuleEngineService(KieContainer kieContainer, AlertService alertService) {
        this.kieContainer = kieContainer;
        this.alertService = alertService;
    }

    public void evaluateRules(SensorDataDTO sensorData) {
        KieSession kieSession = kieContainer.newKieSession();
        try {
            kieSession.setGlobal("alertService", alertService);
            kieSession.insert(sensorData);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
    }
}
