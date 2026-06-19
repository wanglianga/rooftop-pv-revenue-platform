package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.RevenueSettlement;
import com.pvplatform.entity.SettlementCorrection;
import com.pvplatform.service.RevenueSettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/revenue-settlements")
public class RevenueSettlementController {

    @Autowired
    private RevenueSettlementService settlementService;

    @GetMapping
    public Result<List<RevenueSettlement>> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return settlementService.list(status, startDate, endDate);
    }

    @GetMapping("/{id}")
    public Result<RevenueSettlement> getById(@PathVariable Long id) {
        return settlementService.getById(id);
    }

    @GetMapping("/{id}/detail")
    public Result<Map<String, Object>> getSettlementDetail(@PathVariable Long id) {
        return settlementService.getSettlementDetail(id);
    }

    @PostMapping("/generate-7days")
    public Result<RevenueSettlement> generateSettlement7Days() {
        return settlementService.generateSettlement7Days();
    }

    @PostMapping("/generate")
    public Result<RevenueSettlement> generateSettlement(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return settlementService.generateSettlement(startDate, endDate);
    }

    @PutMapping("/{id}/review")
    public Result<RevenueSettlement> reviewSettlement(
            @PathVariable Long id,
            @RequestBody Map<String, Object> params) {
        Long userId = params.get("userId") != null ? Long.valueOf(params.get("userId").toString()) : 1L;
        String remark = params.get("remark") != null ? params.get("remark").toString() : null;
        return settlementService.reviewSettlement(id, userId, remark);
    }

    @PostMapping("/{id}/corrections")
    public Result<SettlementCorrection> addCorrection(
            @PathVariable Long id,
            @RequestBody SettlementCorrection correction) {
        correction.setSettlementId(id);
        return settlementService.addCorrection(correction);
    }

    @GetMapping("/{id}/corrections")
    public Result<List<SettlementCorrection>> getCorrections(@PathVariable Long id) {
        return settlementService.getCorrectionsBySettlementId(id);
    }
}
