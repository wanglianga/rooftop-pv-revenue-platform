package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.PowerCurve;
import com.pvplatform.service.PowerCurveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/power-curves")
public class PowerCurveController {

    @Autowired
    private PowerCurveService powerCurveService;

    @GetMapping
    public Result<List<PowerCurve>> list(
            @RequestParam(required = false) Long inverterId,
            @RequestParam(required = false) String date) {
        return powerCurveService.listByInverterIdAndDate(inverterId, date);
    }

    @PostMapping
    public Result<PowerCurve> save(@RequestBody PowerCurve powerCurve) {
        return powerCurveService.savePowerCurve(powerCurve);
    }

    @GetMapping("/daily")
    public Result<List<Map<String, Object>>> getDailyData(
            @RequestParam Long inverterId,
            @RequestParam String date) {
        return powerCurveService.getDailyCurve(inverterId, date);
    }
}
