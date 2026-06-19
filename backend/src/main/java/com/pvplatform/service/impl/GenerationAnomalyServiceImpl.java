package com.pvplatform.service.impl;

import com.pvplatform.common.Result;
import com.pvplatform.entity.GenerationAnomaly;
import com.pvplatform.entity.GenerationForecast;
import com.pvplatform.entity.SettlementCorrection;
import com.pvplatform.repository.GenerationAnomalyRepository;
import com.pvplatform.repository.GenerationForecastRepository;
import com.pvplatform.repository.SettlementCorrectionRepository;
import com.pvplatform.service.GenerationAnomalyService;
import com.pvplatform.service.GenerationForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GenerationAnomalyServiceImpl implements GenerationAnomalyService {

    @Autowired
    private GenerationAnomalyRepository anomalyRepository;

    @Autowired
    private GenerationForecastRepository forecastRepository;

    @Autowired
    private SettlementCorrectionRepository correctionRepository;

    @Autowired
    @Lazy
    private GenerationForecastService forecastService;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final double DEVIATION_THRESHOLD = 0.15;

    @Override
    public Result<List<GenerationAnomaly>> list(Long inverterId, String status, String startDate, String endDate) {
        try {
            List<GenerationAnomaly> result;
            if (startDate != null && endDate != null) {
                Date start = DATE_FORMAT.parse(startDate);
                Date end = DATE_FORMAT.parse(endDate);
                result = anomalyRepository.findByDateRange(start, end);
            } else if (status != null && !status.isEmpty()) {
                result = anomalyRepository.findByStatus(status);
            } else if (inverterId != null) {
                result = anomalyRepository.findByInverterId(inverterId);
            } else {
                result = anomalyRepository.findAll();
            }
            return Result.success(result);
        } catch (ParseException e) {
            return Result.error("日期格式错误，请使用 yyyy-MM-dd 格式");
        }
    }

    @Override
    public Result<GenerationAnomaly> getById(Long id) {
        Optional<GenerationAnomaly> anomaly = anomalyRepository.findById(id);
        if (anomaly.isPresent()) {
            return Result.success(anomaly.get());
        }
        return Result.error("异常记录不存在");
    }

    @Override
    public Result<GenerationAnomaly> handleAnomaly(Long id, Map<String, Object> params) {
        Optional<GenerationAnomaly> anomalyOpt = anomalyRepository.findById(id);
        if (!anomalyOpt.isPresent()) {
            return Result.error("异常记录不存在");
        }

        GenerationAnomaly anomaly = anomalyOpt.get();
        Date now = new Date();

        String reasonCategory = (String) params.get("deviationReasonCategory");
        String reasonDetail = (String) params.get("detailDescription");
        Long handledBy = params.get("handledBy") != null ? Long.valueOf(params.get("handledBy").toString()) : null;

        if (reasonCategory == null || reasonCategory.isEmpty()) {
            return Result.error("请选择偏差原因类别");
        }

        anomaly.setDeviationReasonCategory(reasonCategory);
        anomaly.setDeviationReason(getReasonLabel(reasonCategory));
        anomaly.setDetailDescription(reasonDetail);
        anomaly.setStatus("RESOLVED");
        anomaly.setHandledBy(handledBy);
        anomaly.setHandleTime(now);
        anomaly.setUpdateTime(now);

        anomaly = anomalyRepository.save(anomaly);

        if (anomaly.getSettlementId() != null) {
            SettlementCorrection correction = new SettlementCorrection();
            correction.setSettlementId(anomaly.getSettlementId());
            correction.setAnomalyId(anomaly.getId());
            correction.setCorrectionType("DEVIATION_REASON");
            correction.setAffectedDate(DATE_FORMAT.format(anomaly.getAnomalyDate()));
            correction.setOriginalForecast(anomaly.getForecastGeneration());
            correction.setCorrectedValue(anomaly.getActualGeneration());
            correction.setAdjustmentAmount(anomaly.getDeviationAmount());
            correction.setReasonCategory(reasonCategory);
            correction.setReasonDetail(reasonDetail);
            correction.setCorrectedBy(handledBy);
            correction.setCorrectionTime(now);
            correction.setCreateTime(now);
            correctionRepository.save(correction);
        }

        return Result.success(anomaly);
    }

    @Override
    public Result<List<GenerationAnomaly>> detectAnomalies(String startDateStr, String endDateStr) {
        try {
            Date startDate = DATE_FORMAT.parse(startDateStr);
            Date endDate = DATE_FORMAT.parse(endDateStr);
            Date now = new Date();

            List<GenerationForecast> forecasts = forecastRepository.findByDateRange(startDate, endDate);
            List<GenerationAnomaly> anomalies = new ArrayList<>();

            for (GenerationForecast forecast : forecasts) {
                if (forecast.getForecastGeneration() == null || forecast.getActualGeneration() == null) {
                    continue;
                }

                double forecastValue = forecast.getForecastGeneration();
                double actualValue = forecast.getActualGeneration();
                double deviation = Math.abs(actualValue - forecastValue) / forecastValue;

                forecast.setDeviationRate(Math.round(deviation * 10000.0) / 100.0);

                if (deviation > DEVIATION_THRESHOLD) {
                    forecast.setIsAnomaly(1);

                    GenerationAnomaly existing = anomalyRepository.findByInverterIdAndDate(
                            forecast.getInverterId(), forecast.getForecastDate());

                    GenerationAnomaly anomaly;
                    if (existing != null) {
                        anomaly = existing;
                    } else {
                        anomaly = new GenerationAnomaly();
                        anomaly.setInverterId(forecast.getInverterId());
                        anomaly.setAnomalyDate(forecast.getForecastDate());
                        anomaly.setForecastId(forecast.getId());
                        anomaly.setSettlementId(forecast.getSettlementId());
                        anomaly.setCreateBy(1L);
                        anomaly.setCreateTime(now);
                        String anomalyNo = "GA" + new SimpleDateFormat("yyyyMMddHHmmss").format(now) + forecast.getInverterId();
                        anomaly.setAnomalyNo(anomalyNo);
                    }

                    anomaly.setForecastGeneration(forecastValue);
                    anomaly.setActualGeneration(actualValue);
                    anomaly.setDeviationRate(Math.round(deviation * 10000.0) / 100.0);
                    anomaly.setDeviationAmount(Math.round((actualValue - forecastValue) * 100.0) / 100.0);

                    if (anomaly.getStatus() == null || anomaly.getStatus().isEmpty()) {
                        anomaly.setStatus("PENDING");
                    }

                    anomaly.setUpdateTime(now);
                    anomalies.add(anomalyRepository.save(anomaly));
                } else {
                    forecast.setIsAnomaly(0);
                }

                forecast.setUpdateTime(now);
                forecastRepository.save(forecast);
            }

            return Result.success(anomalies);
        } catch (ParseException e) {
            return Result.error("日期格式错误，请使用 yyyy-MM-dd 格式");
        }
    }

    @Override
    public Result<Map<String, Object>> getAnomalyStatistics() {
        List<GenerationAnomaly> allAnomalies = anomalyRepository.findAll();

        Map<String, Object> statistics = new HashMap<>();
        long totalCount = allAnomalies.size();
        long pendingCount = allAnomalies.stream().filter(a -> "PENDING".equals(a.getStatus())).count();
        long resolvedCount = allAnomalies.stream().filter(a -> "RESOLVED".equals(a.getStatus())).count();

        Map<String, Long> reasonCategoryCount = new HashMap<>();
        for (GenerationAnomaly a : allAnomalies) {
            if (a.getDeviationReasonCategory() != null) {
                reasonCategoryCount.put(a.getDeviationReasonCategory(),
                        reasonCategoryCount.getOrDefault(a.getDeviationReasonCategory(), 0L) + 1);
            }
        }

        double avgDeviation = allAnomalies.stream()
                .mapToDouble(a -> a.getDeviationRate() != null ? a.getDeviationRate() : 0)
                .average()
                .orElse(0.0);

        statistics.put("totalCount", totalCount);
        statistics.put("pendingCount", pendingCount);
        statistics.put("resolvedCount", resolvedCount);
        statistics.put("avgDeviationRate", Math.round(avgDeviation * 100.0) / 100.0);
        statistics.put("reasonCategoryDistribution", reasonCategoryCount);

        return Result.success(statistics);
    }

    private String getReasonLabel(String category) {
        Map<String, String> map = new HashMap<>();
        map.put("WEATHER_CHANGE", "天气突变");
        map.put("EQUIPMENT_FAILURE", "设备故障");
        map.put("NEW_SHADING", "遮挡新增");
        map.put("GRID_ISSUE", "电网异常");
        map.put("MAINTENANCE", "设备维护");
        map.put("OTHER", "其他原因");
        return map.getOrDefault(category, category);
    }
}
