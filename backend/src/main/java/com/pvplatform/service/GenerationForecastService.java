package com.pvplatform.service;

import com.pvplatform.common.Result;
import com.pvplatform.entity.GenerationForecast;

import java.util.List;
import java.util.Map;

public interface GenerationForecastService {
    Result<List<GenerationForecast>> list(Long inverterId, String startDate, String endDate);
    Result<GenerationForecast> getById(Long id);
    Result<List<GenerationForecast>> generateForecast7Days(Long inverterId);
    Result<List<GenerationForecast>> generateAllForecast7Days();
    Result<Map<String, Object>> getForecastSummary(String startDate, String endDate);
    Result<List<GenerationForecast>> getAnomalyForecasts();
}
