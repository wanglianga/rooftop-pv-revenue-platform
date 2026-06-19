package com.pvplatform.repository;

import com.pvplatform.entity.RevenueSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RevenueSettlementRepository extends JpaRepository<RevenueSettlement, Long> {
    @Query("SELECT s FROM RevenueSettlement s WHERE s.startDate <= :date AND s.endDate >= :date")
    List<RevenueSettlement> findByDateOverlap(@Param("date") Date date);

    @Query("SELECT s FROM RevenueSettlement s WHERE s.status = :status ORDER BY s.createTime DESC")
    List<RevenueSettlement> findByStatus(@Param("status") String status);

    @Query("SELECT s FROM RevenueSettlement s WHERE s.startDate BETWEEN :startDate AND :endDate OR s.endDate BETWEEN :startDate AND :endDate ORDER BY s.startDate")
    List<RevenueSettlement> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    RevenueSettlement findBySettlementNo(String settlementNo);
}
