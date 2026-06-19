package com.pvplatform.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "revenue_allocation")
public class RevenueAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "period")
    private String period;

    @Column(name = "building_no")
    private String buildingNo;

    @Column(name = "total_revenue")
    private Double totalRevenue;

    @Column(name = "allocated_amount")
    private Double allocatedAmount;

    @Column(name = "allocation_rule")
    private String allocationRule;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "area_ratio")
    private Double areaRatio;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "status")
    private Integer status;

    @Column(name = "delayed_flag")
    private Integer delayedFlag;

    @Column(name = "delayed_reason")
    private String delayedReason;

    @Column(name = "delayed_amount")
    private Double delayedAmount;

    @Column(name = "affected_dates")
    private String affectedDates;

    @Column(name = "anomaly_id")
    private Long anomalyId;

    @Column(name = "create_by")
    private Long createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(Double allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public String getAllocationRule() {
        return allocationRule;
    }

    public void setAllocationRule(String allocationRule) {
        this.allocationRule = allocationRule;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Double getAreaRatio() {
        return areaRatio;
    }

    public void setAreaRatio(Double areaRatio) {
        this.areaRatio = areaRatio;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDelayedFlag() {
        return delayedFlag;
    }

    public void setDelayedFlag(Integer delayedFlag) {
        this.delayedFlag = delayedFlag;
    }

    public String getDelayedReason() {
        return delayedReason;
    }

    public void setDelayedReason(String delayedReason) {
        this.delayedReason = delayedReason;
    }

    public Double getDelayedAmount() {
        return delayedAmount;
    }

    public void setDelayedAmount(Double delayedAmount) {
        this.delayedAmount = delayedAmount;
    }

    public String getAffectedDates() {
        return affectedDates;
    }

    public void setAffectedDates(String affectedDates) {
        this.affectedDates = affectedDates;
    }

    public Long getAnomalyId() {
        return anomalyId;
    }

    public void setAnomalyId(Long anomalyId) {
        this.anomalyId = anomalyId;
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
}
