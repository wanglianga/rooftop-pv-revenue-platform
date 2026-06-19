package com.pvplatform.service.impl;

import com.pvplatform.common.Result;
import com.pvplatform.entity.GenerationForecast;
import com.pvplatform.entity.Inverter;
import com.pvplatform.entity.MeterReading;
import com.pvplatform.repository.GenerationForecastRepository;
import com.pvplatform.repository.InverterRepository;
import com.pvplatform.repository.MeterReadingRepository;
import com.pvplatform.service.GenerationForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GenerationForecastServiceImpl implements GenerationForecastService {

    @Autowired
    private GenerationForecastRepository forecastRepository;

    @Autowired
    private InverterRepository inverterRepository;

    @Autowired
    private MeterReadingRepository meterReadingRepository;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final double ANNUAL_DEGRADATION_RATE = 0.008;
    private static final Map<String, Double> WEATHER_FACTORS = new HashMap<>();

    static {
        WEATHER_FACTORS.put("SUNNY", 1.0);
        WEATHER_FACTORS.put("CLOUDY", 0.65);
        WEATHER_FACTORS.put("RAINY", 0.25);
    }

    private static final String[] WEATHER_TYPES = {"SUNNY", "CLOUDY", "RAINY"};

    @Override
    public Result<List<GenerationForecast>> list(Long inverterId, String startDate, String endDate) {
        try {
            List<GenerationForecast> result;
            if (startDate != null && endDate != null) {
                Date start = DATE_FORMAT.parse(startDate);
                Date end = DATE_FORMAT.parse(endDate);
                if (inverterId != null) {
                    result = forecastRepository.findByInverterIdAndDateRange(inverterId, start, end);
                } else {
                    result = forecastRepository.findByDateRange(start, end);
                }
            } else if (inverterId != null) {
                result = forecastRepository.findByInverterId(inverterId);
            } else {
                result = forecastRepository.findAll();
            }
            return Result.success(result);
        } catch (ParseException e) {
            return Result.error("日期格式错误，请使用 yyyy-MM-dd 格式");
        }
    }

    @Override
    public Result<GenerationForecast> getById(Long id) {
        Optional<GenerationForecast> forecast = forecastRepository.findById(id);
        if (forecast.isPresent()) {
            return Result.success(forecast.get());
        }
        return Result.error("预测记录不存在");
    }

    @Override
    public Result<List<GenerationForecast>> generateForecast7Days(Long inverterId) {
        Optional<Inverter> inverterOpt = inverterRepository.findById(inverterId);
        if (!inverterOpt.isPresent()) {
            return Result.error("逆变器不存在");
        }
        Inverter inverter = inverterOpt.get();

        List<GenerationForecast> forecasts = new ArrayList<>();
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        List<MeterReading> historicalReadings = meterReadingRepository.findByInverterId(inverterId);
        double avgDailyGeneration = historicalReadings.stream()
                .mapToDouble(MeterReading::getGeneration)
                .average()
                .orElse(inverter.getCapacity() * 4.5);

        double degradationFactor = calculateDegradationFactor(inverter.getInstallDate(), now);

        Random random = new Random();

        for (int i = 1; i <= 7; i++) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date forecastDate = cal.getTime();

            String weatherType = WEATHER_TYPES[random.nextInt(WEATHER_TYPES.length)];
            double weatherFactor = WEATHER_FACTORS.get(weatherType);

            double baseForecast = avgDailyGeneration * weatherFactor * degradationFactor;
            double randomFactor = 0.9 + random.nextDouble() * 0.2;
            double forecastGeneration = Math.round(baseForecast * randomFactor * 100.0) / 100.0;

            GenerationForecast existing = forecastRepository.findByInverterIdAndDate(inverterId, forecastDate);
            GenerationForecast forecast;
            if (existing != null) {
                forecast = existing;
            } else {
                forecast = new GenerationForecast();
                forecast.setInverterId(inverterId);
                forecast.setForecastDate(forecastDate);
                forecast.setCreateTime(now);
            }

            forecast.setForecastGeneration(forecastGeneration);
            forecast.setWeatherType(weatherType);
            forecast.setWeatherTempHigh(25.0 + random.nextDouble() * 10);
            forecast.setWeatherTempLow(15.0 + random.nextDouble() * 5);
            forecast.setDegradationFactor(Math.round(degradationFactor * 10000.0) / 10000.0);
            forecast.setHistoricalReference("历史同期日均发电量约" + String.format("%.2f", avgDailyGeneration) + "kWh");
            forecast.setIsAnomaly(0);
            forecast.setUpdateTime(now);

            forecasts.add(forecastRepository.save(forecast));
        }

        return Result.success(forecasts);
    }

    @Override
    public Result<List<GenerationForecast>> generateAllForecast7Days() {
        List<Inverter> inverters = inverterRepository.findAll();
        List<GenerationForecast> allForecasts = new ArrayList<>();

        for (Inverter inverter : inverters) {
            Result<List<GenerationForecast>> result = generateForecast7Days(inverter.getId());
            if (result.getCode() == 200) {
                allForecasts.addAll(result.getData());
            }
        }

        return Result.success(allForecasts);
    }

    @Override
    public Result<Map<String, Object>> getForecastSummary(String startDate, String endDate) {
        try {
            Date start = DATE_FORMAT.parse(startDate);
            Date end = DATE_FORMAT.parse(endDate);
            List<GenerationForecast> forecasts = forecastRepository.findByDateRange(start, end);

            Map<String, Object> summary = new HashMap<>();
            double totalForecast = forecasts.stream()
                    .mapToDouble(f -> f.getForecastGeneration() != null ? f.getForecastGeneration() : 0)
                    .sum();
            double totalActual = forecasts.stream()
                    .mapToDouble(f -> f.getActualGeneration() != null ? f.getActualGeneration() : 0)
                    .sum();
            long anomalyCount = forecasts.stream()
                    .filter(f -> f.getIsAnomaly() != null && f.getIsAnomaly() == 1)
                    .count();

            Map<String, Integer> weatherCount = new HashMap<>();
            for (GenerationForecast f : forecasts) {
                weatherCount.put(f.getWeatherType(), weatherCount.getOrDefault(f.getWeatherType(), 0) + 1);
            }

            summary.put("totalForecastGeneration", Math.round(totalForecast * 100.0) / 100.0);
            summary.put("totalActualGeneration", Math.round(totalActual * 100.0) / 100.0);
            summary.put("forecastDays", forecasts.size());
            summary.put("anomalyCount", anomalyCount);
            summary.put("weatherDistribution", weatherCount);

            return Result.success(summary);
        } catch (ParseException e) {
            return Result.error("日期格式错误，请使用 yyyy-MM-dd 格式");
        }
    }

    @Override
    public Result<List<GenerationForecast>> getAnomalyForecasts() {
        return Result.success(forecastRepository.findAllAnomalies());
    }

    private double calculateDegradationFactor(Date installDate, Date now) {
        if (installDate == null) {
            return 0.95;
        }
        long daysDiff = (now.getTime() - installDate.getTime()) / (1000 * 60 * 60 * 24);
        double years = daysDiff / 365.0;
        return Math.pow(1 - ANNUAL_DEGRADATION_RATE, years);
    }
}
