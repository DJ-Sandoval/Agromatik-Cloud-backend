package com.agromatik.cloud.application.port.out;
import com.agromatik.cloud.infrastructure.mongo.AlertDocument;

import java.util.List;

public interface AlertPort {
    AlertDocument save(AlertDocument alert);
    List<AlertDocument> findAll();
    List<AlertDocument> findUnacknowledged();
    AlertDocument acknowledge(String alertId);
}
