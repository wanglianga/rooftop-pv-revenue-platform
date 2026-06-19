package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.InverterAnomaly;
import com.pvplatform.service.InverterAnomalyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inverter-anomalies")
public class InverterAnomalyController {

    @Autowired
    private InverterAnomalyService inverterAnomalyService;

    @GetMapping
    public Result<List<InverterAnomaly>> list(
            @RequestParam(required = false) Long inverterId,
            @RequestParam(required = false) String status) {
        if (inverterId != null) {
            return Result.success(inverterAnomalyService.listByInverterId(inverterId));
        } else if (status != null) {
            return Result.success(inverterAnomalyService.listByStatus(status));
        }
        return Result.success(inverterAnomalyService.listAll());
    }

    @GetMapping("/{id}")
    public Result<InverterAnomaly> getById(@PathVariable Long id) {
        InverterAnomaly anomaly = inverterAnomalyService.getById(id);
        if (anomaly == null) {
            return Result.error("异常记录不存在");
        }
        return Result.success(anomaly);
    }

    @PostMapping
    public Result<InverterAnomaly> create(@RequestBody InverterAnomaly anomaly) {
        return Result.success(inverterAnomalyService.createAnomaly(anomaly));
    }

    @PutMapping("/{id}")
    public Result<InverterAnomaly> update(@PathVariable Long id, @RequestBody InverterAnomaly anomaly) {
        anomaly.setId(id);
        InverterAnomaly result = inverterAnomalyService.updateAnomaly(anomaly);
        if (result == null) {
            return Result.error("异常记录不存在");
        }
        return Result.success(result);
    }

    @PutMapping("/{id}/complete-repair")
    public Result<InverterAnomaly> completeRepair(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String repairResult = params.get("repairResult");
        String beforeCurveDate = params.get("beforeCurveDate");
        String afterCurveDate = params.get("afterCurveDate");
        String curveComparisonResult = params.get("curveComparisonResult");
        InverterAnomaly result = inverterAnomalyService.completeRepair(id, repairResult, beforeCurveDate, afterCurveDate, curveComparisonResult);
        if (result == null) {
            return Result.error("异常记录不存在");
        }
        return Result.success(result);
    }

    @PutMapping("/{id}/close")
    public Result<InverterAnomaly> close(@PathVariable Long id) {
        InverterAnomaly result = inverterAnomalyService.closeAnomaly(id);
        if (result == null) {
            return Result.error("异常记录不存在");
        }
        return Result.success(result);
    }
}
