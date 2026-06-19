package com.pvplatform.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "inverter_anomaly")
public class InverterAnomaly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "inverter_id")
    private Long inverterId;

    @Column(name = "anomaly_type")
    private String anomalyType;

    @Column(name = "description")
    private String description;

    @Column(name = "discovery_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date discoveryTime;

    @Column(name = "affected_pv_strings")
    private String affectedPvStrings;

    @Column(name = "affected_building")
    private String affectedBuilding;

    @Column(name = "downtime_hours")
    private Double downtimeHours;

    @Column(name = "troubleshoot_process")
    private String troubleshootProcess;

    @Column(name = "repair_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date repairTime;

    @Column(name = "repair_result")
    private String repairResult;

    @Column(name = "affected_dates")
    private String affectedDates;

    @Column(name = "before_curve_date")
    private String beforeCurveDate;

    @Column(name = "after_curve_date")
    private String afterCurveDate;

    @Column(name = "curve_comparison_result")
    private String curveComparisonResult;

    @Column(name = "status")
    private String status;

    @Column(name = "operator")
    private String operator;

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

    public Long getInverterId() {
        return inverterId;
    }

    public void setInverterId(Long inverterId) {
        this.inverterId = inverterId;
    }

    public String getAnomalyType() {
        return anomalyType;
    }

    public void setAnomalyType(String anomalyType) {
        this.anomalyType = anomalyType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDiscoveryTime() {
        return discoveryTime;
    }

    public void setDiscoveryTime(Date discoveryTime) {
        this.discoveryTime = discoveryTime;
    }

    public String getAffectedPvStrings() {
        return affectedPvStrings;
    }

    public void setAffectedPvStrings(String affectedPvStrings) {
        this.affectedPvStrings = affectedPvStrings;
    }

    public String getAffectedBuilding() {
        return affectedBuilding;
    }

    public void setAffectedBuilding(String affectedBuilding) {
        this.affectedBuilding = affectedBuilding;
    }

    public Double getDowntimeHours() {
        return downtimeHours;
    }

    public void setDowntimeHours(Double downtimeHours) {
        this.downtimeHours = downtimeHours;
    }

    public String getTroubleshootProcess() {
        return troubleshootProcess;
    }

    public void setTroubleshootProcess(String troubleshootProcess) {
        this.troubleshootProcess = troubleshootProcess;
    }

    public Date getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(Date repairTime) {
        this.repairTime = repairTime;
    }

    public String getRepairResult() {
        return repairResult;
    }

    public void setRepairResult(String repairResult) {
        this.repairResult = repairResult;
    }

    public String getAffectedDates() {
        return affectedDates;
    }

    public void setAffectedDates(String affectedDates) {
        this.affectedDates = affectedDates;
    }

    public String getBeforeCurveDate() {
        return beforeCurveDate;
    }

    public void setBeforeCurveDate(String beforeCurveDate) {
        this.beforeCurveDate = beforeCurveDate;
    }

    public String getAfterCurveDate() {
        return afterCurveDate;
    }

    public void setAfterCurveDate(String afterCurveDate) {
        this.afterCurveDate = afterCurveDate;
    }

    public String getCurveComparisonResult() {
        return curveComparisonResult;
    }

    public void setCurveComparisonResult(String curveComparisonResult) {
        this.curveComparisonResult = curveComparisonResult;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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
