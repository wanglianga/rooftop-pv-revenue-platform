package com.pvplatform.repository;

import com.pvplatform.entity.SettlementCorrection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SettlementCorrectionRepository extends JpaRepository<SettlementCorrection, Long> {
    @Query("SELECT c FROM SettlementCorrection c WHERE c.settlementId = :settlementId ORDER BY c.correctionTime DESC")
    List<SettlementCorrection> findBySettlementId(@Param("settlementId") Long settlementId);

    @Query("SELECT c FROM SettlementCorrection c WHERE c.anomalyId = :anomalyId ORDER BY c.correctionTime DESC")
    List<SettlementCorrection> findByAnomalyId(@Param("anomalyId") Long anomalyId);
}
