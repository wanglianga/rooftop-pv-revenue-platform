package com.pvplatform.service.impl;

import com.pvplatform.common.Result;
import com.pvplatform.entity.PowerCurve;
import com.pvplatform.repository.PowerCurveRepository;
import com.pvplatform.service.PowerCurveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PowerCurveServiceImpl implements PowerCurveService {

    @Autowired
    private PowerCurveRepository powerCurveRepository;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Result<List<PowerCurve>> listByInverterIdAndDate(Long inverterId, String date) {
        if (inverterId != null && date != null) {
            try {
                Date parseDate = DATE_FORMAT.parse(date);
                return Result.success(powerCurveRepository.findByInverterIdAndDate(inverterId, parseDate));
            } catch (ParseException e) {
                return Result.error("日期格式错误，请使用 yyyy-MM-dd 格式");
            }
        }
        return Result.success(powerCurveRepository.findAll());
    }

    @Override
    public Result<PowerCurve> savePowerCurve(PowerCurve powerCurve) {
        return Result.success(powerCurveRepository.save(powerCurve));
    }

    @Override
    public Result<List<Map<String, Object>>> getDailyCurve(Long inverterId, String date) {
        List<PowerCurve> curves;
        try {
            Date parseDate = DATE_FORMAT.parse(date);
            curves = powerCurveRepository.findByInverterIdAndDate(inverterId, parseDate);
        } catch (ParseException e) {
            return Result.error("日期格式错误，请使用 yyyy-MM-dd 格式");
        }

        List<Map<String, Object>> dailyData = new ArrayList<>();
        for (PowerCurve curve : curves) {
            Map<String, Object> item = new HashMap<>();
            item.put("time", curve.getRecordTime());
            item.put("power", curve.getPower());
            item.put("inverterId", curve.getInverterId());
            dailyData.add(item);
        }

        return Result.success(dailyData);
    }
}
