package com.pvplatform.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "settlement_correction")
public class SettlementCorrection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "settlement_id")
    private Long settlementId;

    @Column(name = "anomaly_id")
    private Long anomalyId;

    @Column(name = "correction_type")
    private String correctionType;

    @Column(name = "affected_date")
    private String affectedDate;

    @Column(name = "original_forecast")
    private Double originalForecast;

    @Column(name = "corrected_value")
    private Double correctedValue;

    @Column(name = "adjustment_amount")
    private Double adjustmentAmount;

    @Column(name = "reason_category")
    private String reasonCategory;

    @Column(name = "reason_detail")
    private String reasonDetail;

    @Column(name = "corrected_by")
    private Long correctedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "correction_time")
    private Date correctionTime;

    @Column(name = "remark")
    private String remark;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }

    public Long getAnomalyId() {
        return anomalyId;
    }

    public void setAnomalyId(Long anomalyId) {
        this.anomalyId = anomalyId;
    }

    public String getCorrectionType() {
        return correctionType;
    }

    public void setCorrectionType(String correctionType) {
        this.correctionType = correctionType;
    }

    public String getAffectedDate() {
        return affectedDate;
    }

    public void setAffectedDate(String affectedDate) {
        this.affectedDate = affectedDate;
    }

    public Double getOriginalForecast() {
        return originalForecast;
    }

    public void setOriginalForecast(Double originalForecast) {
        this.originalForecast = originalForecast;
    }

    public Double getCorrectedValue() {
        return correctedValue;
    }

    public void setCorrectedValue(Double correctedValue) {
        this.correctedValue = correctedValue;
    }

    public Double getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(Double adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public String getReasonCategory() {
        return reasonCategory;
    }

    public void setReasonCategory(String reasonCategory) {
        this.reasonCategory = reasonCategory;
    }

    public String getReasonDetail() {
        return reasonDetail;
    }

    public void setReasonDetail(String reasonDetail) {
        this.reasonDetail = reasonDetail;
    }

    public Long getCorrectedBy() {
        return correctedBy;
    }

    public void setCorrectedBy(Long correctedBy) {
        this.correctedBy = correctedBy;
    }

    public Date getCorrectionTime() {
        return correctionTime;
    }

    public void setCorrectionTime(Date correctionTime) {
        this.correctionTime = correctionTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
