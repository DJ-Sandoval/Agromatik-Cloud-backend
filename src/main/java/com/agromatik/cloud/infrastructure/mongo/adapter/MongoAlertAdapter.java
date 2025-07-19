package com.agromatik.cloud.infrastructure.mongo.adapter;
import com.agromatik.cloud.application.port.out.AlertPort;
import com.agromatik.cloud.infrastructure.mongo.AlertDocument;
import com.agromatik.cloud.infrastructure.mongo.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MongoAlertAdapter implements AlertPort {
    private final AlertRepository alertRepository;

    @Override
    public AlertDocument save(AlertDocument alert) {
        return alertRepository.save(alert);
    }

    @Override
    public List<AlertDocument> findAll() {
        return alertRepository.findByOrderByTimestampDesc();
    }

    @Override
    public List<AlertDocument> findUnacknowledged() {
        return alertRepository.findByAcknowledgedFalseOrderByTimestampDesc();
    }

    @Override
    public AlertDocument acknowledge(String alertId) {
        return alertRepository.findById(alertId)
                .map(alert -> {
                    alert.setAcknowledged(true);
                    return alertRepository.save(alert);
                })
                .orElseThrow(() -> new RuntimeException("Alert not found"));
    }
}
