package com.pvplatform.repository;

import com.pvplatform.entity.InverterAnomaly;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InverterAnomalyRepository extends JpaRepository<InverterAnomaly, Long> {
    List<InverterAnomaly> findByInverterId(Long inverterId);
    List<InverterAnomaly> findByStatus(String status);
    List<InverterAnomaly> findByInverterIdOrderByDiscoveryTimeDesc(Long inverterId);
}
