package com.pvplatform.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "revenue_settlement")
public class RevenueSettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "settlement_no")
    private String settlementNo;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "total_forecast_generation")
    private Double totalForecastGeneration;

    @Column(name = "total_actual_generation")
    private Double totalActualGeneration;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "self_use_ratio")
    private Double selfUseRatio;

    @Column(name = "grid_ratio")
    private Double gridRatio;

    @Column(name = "self_use_price")
    private Double selfUsePrice;

    @Column(name = "grid_price")
    private Double gridPrice;

    @Column(name = "forecast_revenue")
    private Double forecastRevenue;

    @Column(name = "actual_revenue")
    private Double actualRevenue;

    @Column(name = "deviation_amount")
    private Double deviationAmount;

    @Column(name = "deviation_rate")
    private Double deviationRate;

    @Column(name = "status")
    private String status;

    @Column(name = "weather_summary")
    private String weatherSummary;

    @Column(name = "forecast_basis")
    private String forecastBasis;

    @Column(name = "remark")
    private String remark;

    @Column(name = "reviewed_by")
    private Long reviewedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "review_time")
    private Date reviewTime;

    @Column(name = "create_by")
    private Long createBy;

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

    public String getSettlementNo() {
        return settlementNo;
    }

    public void setSettlementNo(String settlementNo) {
        this.settlementNo = settlementNo;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getTotalForecastGeneration() {
        return totalForecastGeneration;
    }

    public void setTotalForecastGeneration(Double totalForecastGeneration) {
        this.totalForecastGeneration = totalForecastGeneration;
    }

    public Double getTotalActualGeneration() {
        return totalActualGeneration;
    }

    public void setTotalActualGeneration(Double totalActualGeneration) {
        this.totalActualGeneration = totalActualGeneration;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getSelfUseRatio() {
        return selfUseRatio;
    }

    public void setSelfUseRatio(Double selfUseRatio) {
        this.selfUseRatio = selfUseRatio;
    }

    public Double getGridRatio() {
        return gridRatio;
    }

    public void setGridRatio(Double gridRatio) {
        this.gridRatio = gridRatio;
    }

    public Double getSelfUsePrice() {
        return selfUsePrice;
    }

    public void setSelfUsePrice(Double selfUsePrice) {
        this.selfUsePrice = selfUsePrice;
    }

    public Double getGridPrice() {
        return gridPrice;
    }

    public void setGridPrice(Double gridPrice) {
        this.gridPrice = gridPrice;
    }

    public Double getForecastRevenue() {
        return forecastRevenue;
    }

    public void setForecastRevenue(Double forecastRevenue) {
        this.forecastRevenue = forecastRevenue;
    }

    public Double getActualRevenue() {
        return actualRevenue;
    }

    public void setActualRevenue(Double actualRevenue) {
        this.actualRevenue = actualRevenue;
    }

    public Double getDeviationAmount() {
        return deviationAmount;
    }

    public void setDeviationAmount(Double deviationAmount) {
        this.deviationAmount = deviationAmount;
    }

    public Double getDeviationRate() {
        return deviationRate;
    }

    public void setDeviationRate(Double deviationRate) {
        this.deviationRate = deviationRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWeatherSummary() {
        return weatherSummary;
    }

    public void setWeatherSummary(String weatherSummary) {
        this.weatherSummary = weatherSummary;
    }

    public String getForecastBasis() {
        return forecastBasis;
    }

    public void setForecastBasis(String forecastBasis) {
        this.forecastBasis = forecastBasis;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Long reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
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
