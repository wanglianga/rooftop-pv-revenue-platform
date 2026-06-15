package com.pvplatform.service;

import com.pvplatform.common.Result;
import com.pvplatform.entity.PowerCurve;

import java.util.List;
import java.util.Map;

public interface PowerCurveService {
    Result<List<PowerCurve>> listByInverterIdAndDate(Long inverterId, String date);
    Result<PowerCurve> savePowerCurve(PowerCurve powerCurve);
    Result<List<Map<String, Object>>> getDailyCurve(Long inverterId, String date);
}
