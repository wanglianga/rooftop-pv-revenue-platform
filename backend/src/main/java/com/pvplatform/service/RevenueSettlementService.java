package com.pvplatform.service;

import com.pvplatform.common.Result;
import com.pvplatform.entity.RevenueSettlement;
import com.pvplatform.entity.SettlementCorrection;

import java.util.List;
import java.util.Map;

public interface RevenueSettlementService {
    Result<List<RevenueSettlement>> list(String status, String startDate, String endDate);
    Result<RevenueSettlement> getById(Long id);
    Result<RevenueSettlement> generateSettlement7Days();
    Result<RevenueSettlement> generateSettlement(String startDate, String endDate);
    Result<Map<String, Object>> getSettlementDetail(Long id);
    Result<RevenueSettlement> reviewSettlement(Long id, Long userId, String remark);
    Result<SettlementCorrection> addCorrection(SettlementCorrection correction);
    Result<List<SettlementCorrection>> getCorrectionsBySettlementId(Long settlementId);
}
