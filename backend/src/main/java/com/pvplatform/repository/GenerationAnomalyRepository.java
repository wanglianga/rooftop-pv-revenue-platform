package com.pvplatform.repository;

import com.pvplatform.entity.GenerationAnomaly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface GenerationAnomalyRepository extends JpaRepository<GenerationAnomaly, Long> {
    @Query("SELECT a FROM GenerationAnomaly a WHERE a.inverterId = :inverterId ORDER BY a.anomalyDate DESC")
    List<GenerationAnomaly> findByInverterId(@Param("inverterId") Long inverterId);

    @Query("SELECT a FROM GenerationAnomaly a WHERE a.status = :status ORDER BY a.createTime DESC")
    List<GenerationAnomaly> findByStatus(@Param("status") String status);

    @Query("SELECT a FROM GenerationAnomaly a WHERE a.anomalyDate BETWEEN :startDate AND :endDate ORDER BY a.anomalyDate DESC")
    List<GenerationAnomaly> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT a FROM GenerationAnomaly a WHERE a.settlementId = :settlementId ORDER BY a.anomalyDate")
    List<GenerationAnomaly> findBySettlementId(@Param("settlementId") Long settlementId);

    @Query("SELECT a FROM GenerationAnomaly a WHERE a.inverterId = :inverterId AND a.anomalyDate = :date")
    GenerationAnomaly findByInverterIdAndDate(@Param("inverterId") Long inverterId, @Param("date") Date date);

    GenerationAnomaly findByAnomalyNo(String anomalyNo);
}
