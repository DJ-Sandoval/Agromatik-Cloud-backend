package com.agromatik.cloud.infrastructure.mysql.repository;

import com.agromatik.cloud.domain.model.Recomendacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringRecomendacionRepository extends JpaRepository<Recomendacion, Long> {
    List<Recomendacion> findByAlertaId(Long alertaId);
    List<Recomendacion> findByImplementadaFalse();
}
