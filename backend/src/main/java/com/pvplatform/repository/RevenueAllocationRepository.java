package com.pvplatform.repository;

import com.pvplatform.entity.RevenueAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RevenueAllocationRepository extends JpaRepository<RevenueAllocation, Long> {
    List<RevenueAllocation> findByPeriod(String period);
    List<RevenueAllocation> findByOwnerId(Long ownerId);
    List<RevenueAllocation> findByDelayedFlag(Integer delayedFlag);
    List<RevenueAllocation> findByAnomalyId(Long anomalyId);
}
