package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.GenerationAnomaly;
import com.pvplatform.service.GenerationAnomalyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/generation-anomalies")
public class GenerationAnomalyController {

    @Autowired
    private GenerationAnomalyService anomalyService;

    @GetMapping
    public Result<List<GenerationAnomaly>> list(
            @RequestParam(required = false) Long inverterId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return anomalyService.list(inverterId, status, startDate, endDate);
    }

    @GetMapping("/{id}")
    public Result<GenerationAnomaly> getById(@PathVariable Long id) {
        return anomalyService.getById(id);
    }

    @PutMapping("/{id}/handle")
    public Result<GenerationAnomaly> handleAnomaly(
            @PathVariable Long id,
            @RequestBody Map<String, Object> params) {
        return anomalyService.handleAnomaly(id, params);
    }

    @PostMapping("/detect")
    public Result<List<GenerationAnomaly>> detectAnomalies(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return anomalyService.detectAnomalies(startDate, endDate);
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getAnomalyStatistics() {
        return anomalyService.getAnomalyStatistics();
    }
}
