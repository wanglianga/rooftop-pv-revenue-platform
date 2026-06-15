package com.pvplatform.service;

import com.pvplatform.common.Result;
import com.pvplatform.entity.MeterReading;

import java.util.List;
import java.util.Map;

public interface MeterReadingService {
    Result<List<MeterReading>> listByInverterId(Long inverterId);
    Result<MeterReading> saveMeterReading(MeterReading meterReading);
    Result<List<Map<String, Object>>> getMonthlySummary(Long inverterId, String month);
}
