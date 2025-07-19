package com.agromatik.cloud.infrastructure.mongo.repository;

import com.agromatik.cloud.infrastructure.mongo.AlertDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends MongoRepository<AlertDocument, String> {
    List<AlertDocument> findByOrderByTimestampDesc();
    List<AlertDocument> findByAcknowledgedFalseOrderByTimestampDesc();
}