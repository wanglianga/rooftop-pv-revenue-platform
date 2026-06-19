package com.pvplatform.service.impl;

import com.pvplatform.common.Result;
import com.pvplatform.entity.*;
import com.pvplatform.repository.*;
import com.pvplatform.service.GenerationForecastService;
import com.pvplatform.service.RevenueSettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RevenueSettlementServiceImpl implements RevenueSettlementService {

    @Autowired
    private RevenueSettlementRepository settlementRepository;

    @Autowired
    private GenerationForecastRepository forecastRepository;

    @Autowired
    private GenerationAnomalyRepository anomalyRepository;

    @Autowired
    private SettlementCorrectionRepository correctionRepository;

    @Autowired
    private InverterRepository inverterRepository;

    @Autowired
    @Lazy
    private GenerationForecastService forecastService;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final double SELF_USE_PRICE = 0.56;
    private static final double GRID_PRICE = 0.41;
    private static final double SELF_USE_RATIO = 0.68;
    private static final double GRID_RATIO = 0.32;

    @Override
    public Result<List<RevenueSettlement>> list(String status, String startDate, String endDate) {
        try {
            List<RevenueSettlement> result;
            if (startDate != null && endDate != null) {
                Date start = DATE_FORMAT.parse(startDate);
                Date end = DATE_FORMAT.parse(endDate);
                result = settlementRepository.findByDateRange(start, end);
            } else if (status != null && !status.isEmpty()) {
                result = settlementRepository.findByStatus(status);
            } else {
                result = settlementRepository.findAll();
            }
            result.sort((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()));
            return Result.success(result);
        } catch (ParseException e) {
            return Result.error("日期格式错误，请使用 yyyy-MM-dd 格式");
        }
    }

    @Override
    public Result<RevenueSettlement> getById(Long id) {
        Optional<RevenueSettlement> settlement = settlementRepository.findById(id);
        if (settlement.isPresent()) {
            return Result.success(settlement.get());
        }
        return Result.error("预结算单不存在");
    }

    @Override
    public Result<RevenueSettlement> generateSettlement7Days() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date endDate = cal.getTime();
        return generateSettlement(DATE_FORMAT.format(startDate), DATE_FORMAT.format(endDate));
    }

    @Override
    public Result<RevenueSettlement> generateSettlement(String startDateStr, String endDateStr) {
        try {
            Date startDate = DATE_FORMAT.parse(startDateStr);
            Date endDate = DATE_FORMAT.parse(endDateStr);
            Date now = new Date();

            List<Inverter> inverters = inverterRepository.findAll();
            List<GenerationForecast> allForecasts = new ArrayList<>();

            for (Inverter inverter : inverters) {
                List<GenerationForecast> forecasts = forecastRepository.findByInverterIdAndDateRange(
                        inverter.getId(), startDate, endDate);
                if (forecasts.isEmpty()) {
                    Result<List<GenerationForecast>> result = forecastService.generateForecast7Days(inverter.getId());
                    if (result.getCode() == 200) {
                        forecasts = result.getData();
                    }
                }
                allForecasts.addAll(forecasts);
            }

            double totalForecast = allForecasts.stream()
                    .mapToDouble(f -> f.getForecastGeneration() != null ? f.getForecastGeneration() : 0)
                    .sum();
            double totalActual = allForecasts.stream()
                    .mapToDouble(f -> f.getActualGeneration() != null ? f.getActualGeneration() : 0)
                    .sum();

            double forecastRevenue = calculateRevenue(totalForecast);
            double actualRevenue = calculateRevenue(totalActual);

            Set<String> weatherTypes = new HashSet<>();
            for (GenerationForecast f : allForecasts) {
                if (f.getWeatherType() != null) {
                    weatherTypes.add(getWeatherLabel(f.getWeatherType()));
                }
            }
            String weatherSummary = String.join("、", weatherTypes);

            StringBuilder forecastBasis = new StringBuilder();
            forecastBasis.append("1. 基于历史发电曲线数据，参考同期日均发电量；");
            forecastBasis.append("2. 未来天气预测：").append(weatherSummary).append("；");
            forecastBasis.append("3. 组件衰减系数：0.8%/年；");
            forecastBasis.append("4. 电价标准：自用").append(SELF_USE_PRICE).append("元/度，上网").append(GRID_PRICE).append("元/度");

            Calendar cal = Calendar.getInstance();
            String settlementNo = "ST" + new SimpleDateFormat("yyyyMMddHHmmss").format(now);

            RevenueSettlement settlement = new RevenueSettlement();
            settlement.setSettlementNo(settlementNo);
            settlement.setStartDate(startDate);
            settlement.setEndDate(endDate);
            settlement.setTotalForecastGeneration(Math.round(totalForecast * 100.0) / 100.0);
            settlement.setTotalActualGeneration(Math.round(totalActual * 100.0) / 100.0);
            settlement.setUnitPrice(Math.round(((SELF_USE_PRICE * SELF_USE_RATIO) + (GRID_PRICE * GRID_RATIO)) * 10000.0) / 10000.0);
            settlement.setSelfUseRatio(SELF_USE_RATIO);
            settlement.setGridRatio(GRID_RATIO);
            settlement.setSelfUsePrice(SELF_USE_PRICE);
            settlement.setGridPrice(GRID_PRICE);
            settlement.setForecastRevenue(Math.round(forecastRevenue * 100.0) / 100.0);
            settlement.setActualRevenue(Math.round(actualRevenue * 100.0) / 100.0);
            settlement.setDeviationAmount(Math.round((actualRevenue - forecastRevenue) * 100.0) / 100.0);
            settlement.setDeviationRate(forecastRevenue > 0 ? Math.round((actualRevenue - forecastRevenue) / forecastRevenue * 10000.0) / 100.0 : 0);
            settlement.setStatus("DRAFT");
            settlement.setWeatherSummary(weatherSummary);
            settlement.setForecastBasis(forecastBasis.toString());
            settlement.setCreateBy(1L);
            settlement.setCreateTime(now);
            settlement.setUpdateTime(now);

            settlement = settlementRepository.save(settlement);

            for (GenerationForecast f : allForecasts) {
                f.setSettlementId(settlement.getId());
                f.setUpdateTime(now);
                forecastRepository.save(f);
            }

            return Result.success(settlement);
        } catch (ParseException e) {
            return Result.error("日期格式错误，请使用 yyyy-MM-dd 格式");
        }
    }

    @Override
    public Result<Map<String, Object>> getSettlementDetail(Long id) {
        Optional<RevenueSettlement> settlementOpt = settlementRepository.findById(id);
        if (!settlementOpt.isPresent()) {
            return Result.error("预结算单不存在");
        }

        RevenueSettlement settlement = settlementOpt.get();
        List<GenerationForecast> forecasts = forecastRepository.findBySettlementId(id);
        List<GenerationAnomaly> anomalies = anomalyRepository.findBySettlementId(id);
        List<SettlementCorrection> corrections = correctionRepository.findBySettlementId(id);

        Map<String, Object> detail = new HashMap<>();
        detail.put("settlement", settlement);
        detail.put("forecasts", forecasts);
        detail.put("anomalies", anomalies);
        detail.put("corrections", corrections);

        return Result.success(detail);
    }

    @Override
    public Result<RevenueSettlement> reviewSettlement(Long id, Long userId, String remark) {
        Optional<RevenueSettlement> settlementOpt = settlementRepository.findById(id);
        if (!settlementOpt.isPresent()) {
            return Result.error("预结算单不存在");
        }

        RevenueSettlement settlement = settlementOpt.get();
        settlement.setStatus("REVIEWED");
        settlement.setReviewedBy(userId);
        settlement.setReviewTime(new Date());
        if (remark != null && !remark.isEmpty()) {
            settlement.setRemark(remark);
        }
        settlement.setUpdateTime(new Date());

        return Result.success(settlementRepository.save(settlement));
    }

    @Override
    public Result<SettlementCorrection> addCorrection(SettlementCorrection correction) {
        correction.setCorrectionTime(new Date());
        correction.setCreateTime(new Date());

        SettlementCorrection saved = correctionRepository.save(correction);

        if (correction.getSettlementId() != null) {
            Optional<RevenueSettlement> settlementOpt = settlementRepository.findById(correction.getSettlementId());
            if (settlementOpt.isPresent()) {
                RevenueSettlement settlement = settlementOpt.get();
                settlement.setUpdateTime(new Date());
                settlementRepository.save(settlement);
            }
        }

        return Result.success(saved);
    }

    @Override
    public Result<List<SettlementCorrection>> getCorrectionsBySettlementId(Long settlementId) {
        return Result.success(correctionRepository.findBySettlementId(settlementId));
    }

    private double calculateRevenue(double generation) {
        double selfUseAmount = generation * SELF_USE_RATIO * SELF_USE_PRICE;
        double gridAmount = generation * GRID_RATIO * GRID_PRICE;
        return selfUseAmount + gridAmount;
    }

    private String getWeatherLabel(String type) {
        Map<String, String> map = new HashMap<>();
        map.put("SUNNY", "晴天");
        map.put("CLOUDY", "多云");
        map.put("RAINY", "雨天");
        return map.getOrDefault(type, type);
    }
}
