package com.pvplatform.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "grid_application")
public class GridApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "roof_area_id")
    private Long roofAreaId;

    @Column(name = "applicant")
    private String applicant;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "application_date")
    private Date applicationDate;

    @Column(name = "status")
    private String status;

    @Column(name = "grid_no")
    private String gridNo;

    @Column(name = "acceptance_opinion")
    private String acceptanceOpinion;

    @Column(name = "feedback_by")
    private Long feedbackBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "feedback_time")
    private Date feedbackTime;

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

    public Long getRoofAreaId() {
        return roofAreaId;
    }

    public void setRoofAreaId(Long roofAreaId) {
        this.roofAreaId = roofAreaId;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGridNo() {
        return gridNo;
    }

    public void setGridNo(String gridNo) {
        this.gridNo = gridNo;
    }

    public String getAcceptanceOpinion() {
        return acceptanceOpinion;
    }

    public void setAcceptanceOpinion(String acceptanceOpinion) {
        this.acceptanceOpinion = acceptanceOpinion;
    }

    public Long getFeedbackBy() {
        return feedbackBy;
    }

    public void setFeedbackBy(Long feedbackBy) {
        this.feedbackBy = feedbackBy;
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
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
