package com.pvplatform.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "generation_forecast")
public class GenerationForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "inverter_id")
    private Long inverterId;

    @Temporal(TemporalType.DATE)
    @Column(name = "forecast_date")
    private Date forecastDate;

    @Column(name = "forecast_generation")
    private Double forecastGeneration;

    @Column(name = "actual_generation")
    private Double actualGeneration;

    @Column(name = "weather_type")
    private String weatherType;

    @Column(name = "weather_temp_high")
    private Double weatherTempHigh;

    @Column(name = "weather_temp_low")
    private Double weatherTempLow;

    @Column(name = "degradation_factor")
    private Double degradationFactor;

    @Column(name = "historical_reference")
    private String historicalReference;

    @Column(name = "deviation_rate")
    private Double deviationRate;

    @Column(name = "is_anomaly")
    private Integer isAnomaly;

    @Column(name = "settlement_id")
    private Long settlementId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInverterId() {
        return inverterId;
    }

    public void setInverterId(Long inverterId) {
        this.inverterId = inverterId;
    }

    public Date getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(Date forecastDate) {
        this.forecastDate = forecastDate;
    }

    public Double getForecastGeneration() {
        return forecastGeneration;
    }

    public void setForecastGeneration(Double forecastGeneration) {
        this.forecastGeneration = forecastGeneration;
    }

    public Double getActualGeneration() {
        return actualGeneration;
    }

    public void setActualGeneration(Double actualGeneration) {
        this.actualGeneration = actualGeneration;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public Double getWeatherTempHigh() {
        return weatherTempHigh;
    }

    public void setWeatherTempHigh(Double weatherTempHigh) {
        this.weatherTempHigh = weatherTempHigh;
    }

    public Double getWeatherTempLow() {
        return weatherTempLow;
    }

    public void setWeatherTempLow(Double weatherTempLow) {
        this.weatherTempLow = weatherTempLow;
    }

    public Double getDegradationFactor() {
        return degradationFactor;
    }

    public void setDegradationFactor(Double degradationFactor) {
        this.degradationFactor = degradationFactor;
    }

    public String getHistoricalReference() {
        return historicalReference;
    }

    public void setHistoricalReference(String historicalReference) {
        this.historicalReference = historicalReference;
    }

    public Double getDeviationRate() {
        return deviationRate;
    }

    public void setDeviationRate(Double deviationRate) {
        this.deviationRate = deviationRate;
    }

    public Integer getIsAnomaly() {
        return isAnomaly;
    }

    public void setIsAnomaly(Integer isAnomaly) {
        this.isAnomaly = isAnomaly;
    }

    public Long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
