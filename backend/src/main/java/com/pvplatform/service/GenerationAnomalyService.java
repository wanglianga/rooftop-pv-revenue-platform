package com.pvplatform.service;

import com.pvplatform.common.Result;
import com.pvplatform.entity.GenerationAnomaly;

import java.util.List;
import java.util.Map;

public interface GenerationAnomalyService {
    Result<List<GenerationAnomaly>> list(Long inverterId, String status, String startDate, String endDate);
    Result<GenerationAnomaly> getById(Long id);
    Result<GenerationAnomaly> handleAnomaly(Long id, Map<String, Object> params);
    Result<List<GenerationAnomaly>> detectAnomalies(String startDate, String endDate);
    Result<Map<String, Object>> getAnomalyStatistics();
}
