package com.agromatik.cloud.application.port.out;

import com.agromatik.cloud.domain.model.Cultivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CultivoRepository extends JpaRepository<Cultivo, Long> {
    Page<Cultivo> findByUserId(Long userId, Pageable pageable);
}
