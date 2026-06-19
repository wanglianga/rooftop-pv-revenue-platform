package com.pvplatform.repository;

import com.pvplatform.entity.GenerationForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface GenerationForecastRepository extends JpaRepository<GenerationForecast, Long> {
    List<GenerationForecast> findByInverterId(Long inverterId);

    @Query("SELECT f FROM GenerationForecast f WHERE f.inverterId = :inverterId AND f.forecastDate BETWEEN :startDate AND :endDate ORDER BY f.forecastDate")
    List<GenerationForecast> findByInverterIdAndDateRange(@Param("inverterId") Long inverterId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT f FROM GenerationForecast f WHERE f.forecastDate BETWEEN :startDate AND :endDate ORDER BY f.inverterId, f.forecastDate")
    List<GenerationForecast> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT f FROM GenerationForecast f WHERE f.settlementId = :settlementId ORDER BY f.forecastDate")
    List<GenerationForecast> findBySettlementId(@Param("settlementId") Long settlementId);

    @Query("SELECT f FROM GenerationForecast f WHERE f.inverterId = :inverterId AND f.forecastDate = :date")
    GenerationForecast findByInverterIdAndDate(@Param("inverterId") Long inverterId, @Param("date") Date date);

    @Query("SELECT f FROM GenerationForecast f WHERE f.isAnomaly = 1 ORDER BY f.forecastDate DESC")
    List<GenerationForecast> findAllAnomalies();
}
