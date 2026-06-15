package com.pvplatform.service;

import com.pvplatform.entity.RevenueAllocation;
import com.pvplatform.repository.RevenueAllocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RevenueAllocationService {

    private RevenueAllocationRepository revenueAllocationRepository;

    public RevenueAllocationRepository getRevenueAllocationRepository() {
        return revenueAllocationRepository;
    }

    @Autowired
    public void setRevenueAllocationRepository(RevenueAllocationRepository revenueAllocationRepository) {
        this.revenueAllocationRepository = revenueAllocationRepository;
    }

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
}
