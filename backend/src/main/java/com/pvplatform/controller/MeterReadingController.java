package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.MeterReading;
import com.pvplatform.service.MeterReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/meter-readings")
public class MeterReadingController {

    @Autowired
    private MeterReadingService meterReadingService;

    @GetMapping
    public Result<List<MeterReading>> list(@RequestParam(required = false) Long inverterId) {
        return meterReadingService.listByInverterId(inverterId);
    }

    @PostMapping
    public Result<MeterReading> save(@RequestBody MeterReading meterReading) {
        return meterReadingService.saveMeterReading(meterReading);
    }

    @GetMapping("/monthly-summary")
    public Result<List<Map<String, Object>>> getMonthlySummary(
            @RequestParam(required = false) Long inverterId,
            @RequestParam(required = false) String month) {
        return meterReadingService.getMonthlySummary(inverterId, month);
    }
}
