package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.GenerationForecast;
import com.pvplatform.service.GenerationForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/generation-forecasts")
public class GenerationForecastController {

    @Autowired
    private GenerationForecastService forecastService;

    @GetMapping
    public Result<List<GenerationForecast>> list(
            @RequestParam(required = false) Long inverterId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return forecastService.list(inverterId, startDate, endDate);
    }

    @GetMapping("/{id}")
    public Result<GenerationForecast> getById(@PathVariable Long id) {
        return forecastService.getById(id);
    }

    @PostMapping("/generate/{inverterId}")
    public Result<List<GenerationForecast>> generateForecast7Days(@PathVariable Long inverterId) {
        return forecastService.generateForecast7Days(inverterId);
    }

    @PostMapping("/generate-all")
    public Result<List<GenerationForecast>> generateAllForecast7Days() {
        return forecastService.generateAllForecast7Days();
    }

    @GetMapping("/summary")
    public Result<Map<String, Object>> getForecastSummary(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return forecastService.getForecastSummary(startDate, endDate);
    }

    @GetMapping("/anomalies")
    public Result<List<GenerationForecast>> getAnomalyForecasts() {
        return forecastService.getAnomalyForecasts();
    }
}
