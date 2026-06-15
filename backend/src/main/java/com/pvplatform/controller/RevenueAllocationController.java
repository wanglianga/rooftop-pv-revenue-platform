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
            @RequestParam(required = false) Long ownerId) {
        if (period != null) {
            return Result.success(revenueAllocationService.listByPeriod(period));
        } else if (ownerId != null) {
            return Result.success(revenueAllocationService.listByOwnerId(ownerId));
        }
        return Result.error("请提供 period 或 ownerId 参数");
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
}
