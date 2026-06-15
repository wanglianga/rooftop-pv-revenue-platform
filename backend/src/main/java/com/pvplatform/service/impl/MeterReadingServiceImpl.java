package com.pvplatform.service.impl;

import com.pvplatform.common.Result;
import com.pvplatform.entity.MeterReading;
import com.pvplatform.repository.MeterReadingRepository;
import com.pvplatform.service.MeterReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MeterReadingServiceImpl implements MeterReadingService {

    @Autowired
    private MeterReadingRepository meterReadingRepository;

    @Override
    public Result<List<MeterReading>> listByInverterId(Long inverterId) {
        if (inverterId != null) {
            return Result.success(meterReadingRepository.findByInverterId(inverterId));
        }
        return Result.success(meterReadingRepository.findAll());
    }

    @Override
    public Result<MeterReading> saveMeterReading(MeterReading meterReading) {
        return Result.success(meterReadingRepository.save(meterReading));
    }

    @Override
    public Result<List<Map<String, Object>>> getMonthlySummary(Long inverterId, String month) {
        List<MeterReading> readings;
        if (month != null && month.contains("-")) {
            String[] parts = month.split("-");
            Integer year = Integer.parseInt(parts[0]);
            Integer monthNum = Integer.parseInt(parts[1]);
            readings = meterReadingRepository.findByYearAndMonth(year, monthNum);
        } else {
            readings = meterReadingRepository.findAll();
        }

        if (inverterId != null) {
            readings = readings.stream()
                    .filter(r -> r.getInverterId().equals(inverterId))
                    .toList();
        }

        List<Map<String, Object>> summary = new ArrayList<>();
        Map<Long, Double> inverterTotal = new HashMap<>();
        for (MeterReading reading : readings) {
            Long invId = reading.getInverterId();
            Double generation = reading.getGeneration() != null ? reading.getGeneration() : 0.0;
            inverterTotal.put(invId, inverterTotal.getOrDefault(invId, 0.0) + generation);
        }

        for (Map.Entry<Long, Double> entry : inverterTotal.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("inverterId", entry.getKey());
            item.put("totalGeneration", entry.getValue());
            item.put("month", month);
            summary.add(item);
        }

        return Result.success(summary);
    }
}
