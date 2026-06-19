package com.pvplatform.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "generation_anomaly")
public class GenerationAnomaly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "anomaly_no")
    private String anomalyNo;

    @Column(name = "inverter_id")
    private Long inverterId;

    @Temporal(TemporalType.DATE)
    @Column(name = "anomaly_date")
    private Date anomalyDate;

    @Column(name = "forecast_generation")
    private Double forecastGeneration;

    @Column(name = "actual_generation")
    private Double actualGeneration;

    @Column(name = "deviation_rate")
    private Double deviationRate;

    @Column(name = "deviation_amount")
    private Double deviationAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "deviation_reason")
    private String deviationReason;

    @Column(name = "deviation_reason_category")
    private String deviationReasonCategory;

    @Column(name = "detail_description")
    private String detailDescription;

    @Column(name = "forecast_id")
    private Long forecastId;

    @Column(name = "settlement_id")
    private Long settlementId;

    @Column(name = "handled_by")
    private Long handledBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "handle_time")
    private Date handleTime;

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

    public String getAnomalyNo() {
        return anomalyNo;
    }

    public void setAnomalyNo(String anomalyNo) {
        this.anomalyNo = anomalyNo;
    }

    public Long getInverterId() {
        return inverterId;
    }

    public void setInverterId(Long inverterId) {
        this.inverterId = inverterId;
    }

    public Date getAnomalyDate() {
        return anomalyDate;
    }

    public void setAnomalyDate(Date anomalyDate) {
        this.anomalyDate = anomalyDate;
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

    public Double getDeviationRate() {
        return deviationRate;
    }

    public void setDeviationRate(Double deviationRate) {
        this.deviationRate = deviationRate;
    }

    public Double getDeviationAmount() {
        return deviationAmount;
    }

    public void setDeviationAmount(Double deviationAmount) {
        this.deviationAmount = deviationAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeviationReason() {
        return deviationReason;
    }

    public void setDeviationReason(String deviationReason) {
        this.deviationReason = deviationReason;
    }

    public String getDeviationReasonCategory() {
        return deviationReasonCategory;
    }

    public void setDeviationReasonCategory(String deviationReasonCategory) {
        this.deviationReasonCategory = deviationReasonCategory;
    }

    public String getDetailDescription() {
        return detailDescription;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    public Long getForecastId() {
        return forecastId;
    }

    public void setForecastId(Long forecastId) {
        this.forecastId = forecastId;
    }

    public Long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }

    public Long getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(Long handledBy) {
        this.handledBy = handledBy;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
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
