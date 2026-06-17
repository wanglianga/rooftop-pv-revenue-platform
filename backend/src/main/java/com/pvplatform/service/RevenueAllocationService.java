package com.pvplatform.service;

import com.pvplatform.entity.RevenueAllocation;
import com.pvplatform.repository.RevenueAllocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RevenueAllocationService {

    @Autowired
    private RevenueAllocationRepository revenueAllocationRepository;

    public List<RevenueAllocation> calculateAllocation(String period, String rule) {
        List<RevenueAllocation> allocations = new ArrayList<>();
        return allocations;
    }

    public List<RevenueAllocation> listByPeriod(String period) {
        return revenueAllocationRepository.findByPeriod(period);
    }

    public List<RevenueAllocation> listByOwnerId(Long ownerId) {
        return revenueAllocationRepository.findByOwnerId(ownerId);
    }

    public RevenueAllocation saveAllocation(RevenueAllocation allocation) {
        return revenueAllocationRepository.save(allocation);
    }

    public List<RevenueAllocation> listAll() {
        return revenueAllocationRepository.findAll();
    }

    public List<RevenueAllocation> listDelayed() {
        return revenueAllocationRepository.findByDelayedFlag(1);
    }

    public RevenueAllocation markDelayed(Long id, String reason, Double delayedAmount) {
        return revenueAllocationRepository.findById(id).map(allocation -> {
            allocation.setDelayedFlag(1);
            allocation.setDelayedReason(reason);
            allocation.setDelayedAmount(delayedAmount);
            return revenueAllocationRepository.save(allocation);
        }).orElse(null);
    }

    public RevenueAllocation markAffectedDates(Long id, String affectedDates, Long anomalyId) {
        return revenueAllocationRepository.findById(id).map(allocation -> {
            allocation.setAffectedDates(affectedDates);
            allocation.setAnomalyId(anomalyId);
            if (anomalyId != null) {
                allocation.setDelayedFlag(1);
                allocation.setDelayedReason("逆变器异常导致发电受影响");
            }
            return revenueAllocationRepository.save(allocation);
        }).orElse(null);
    }

    public List<RevenueAllocation> listByAnomalyId(Long anomalyId) {
        return revenueAllocationRepository.findByAnomalyId(anomalyId);
    }
}
