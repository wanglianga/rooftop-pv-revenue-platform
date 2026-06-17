package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.RevenueAllocation;
import com.pvplatform.service.RevenueAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/revenue-allocations")
public class RevenueAllocationController {

    @Autowired
    private RevenueAllocationService revenueAllocationService;

    @GetMapping
    public Result<List<RevenueAllocation>> list(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) Integer delayedFlag) {
        if (delayedFlag != null) {
            return Result.success(revenueAllocationService.listDelayed());
        } else if (period != null) {
            return Result.success(revenueAllocationService.listByPeriod(period));
        } else if (ownerId != null) {
            return Result.success(revenueAllocationService.listByOwnerId(ownerId));
        }
        return Result.success(revenueAllocationService.listAll());
    }

    @PostMapping("/calculate")
    public Result<List<RevenueAllocation>> calculate(@RequestBody Map<String, Object> params) {
        String period = (String) params.get("period");
        String allocationRule = (String) params.get("allocationRule");
        return Result.success(revenueAllocationService.calculateAllocation(period, allocationRule));
    }

    @PostMapping
    public Result<RevenueAllocation> save(@RequestBody RevenueAllocation revenueAllocation) {
        return Result.success(revenueAllocationService.saveAllocation(revenueAllocation));
    }

    @PutMapping("/{id}/mark-delayed")
    public Result<RevenueAllocation> markDelayed(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        String reason = (String) params.get("reason");
        Double delayedAmount = params.get("delayedAmount") != null ?
                ((Number) params.get("delayedAmount")).doubleValue() : null;
        RevenueAllocation result = revenueAllocationService.markDelayed(id, reason, delayedAmount);
        if (result == null) {
            return Result.error("收益分配记录不存在");
        }
        return Result.success(result);
    }

    @PutMapping("/{id}/mark-affected")
    public Result<RevenueAllocation> markAffected(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        String affectedDates = (String) params.get("affectedDates");
        Long anomalyId = params.get("anomalyId") != null ?
                ((Number) params.get("anomalyId")).longValue() : null;
        RevenueAllocation result = revenueAllocationService.markAffectedDates(id, affectedDates, anomalyId);
        if (result == null) {
            return Result.error("收益分配记录不存在");
        }
        return Result.success(result);
    }

    @GetMapping("/by-anomaly/{anomalyId}")
    public Result<List<RevenueAllocation>> listByAnomalyId(@PathVariable Long anomalyId) {
        return Result.success(revenueAllocationService.listByAnomalyId(anomalyId));
    }
}
