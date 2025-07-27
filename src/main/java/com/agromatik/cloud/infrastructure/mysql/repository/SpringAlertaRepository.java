package com.agromatik.cloud.infrastructure.mysql.repository;

import com.agromatik.cloud.domain.model.Alerta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpringAlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByLeidaFalse();
    Page<Alerta> findByLeida(boolean leida, Pageable pageable);
    List<Alerta> findByLeida(boolean leida); // Nuevo método para el batch update
}
