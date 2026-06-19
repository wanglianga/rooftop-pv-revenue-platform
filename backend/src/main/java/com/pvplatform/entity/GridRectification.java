package com.pvplatform.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "grid_rectification")
public class GridRectification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "grid_application_id")
    private Long gridApplicationId;

    @Column(name = "rectification_type")
    private String rectificationType;

    @Column(name = "description")
    private String description;

    @Column(name = "photos")
    private String photos;

    @Column(name = "file_version")
    private String fileVersion;

    @Column(name = "retest_result")
    private String retestResult;

    @Column(name = "submitter")
    private String submitter;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "submit_time")
    private Date submitTime;

    @Column(name = "status")
    private String status;

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

    public Long getGridApplicationId() {
        return gridApplicationId;
    }

    public void setGridApplicationId(Long gridApplicationId) {
        this.gridApplicationId = gridApplicationId;
    }

    public String getRectificationType() {
        return rectificationType;
    }

    public void setRectificationType(String rectificationType) {
        this.rectificationType = rectificationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(String fileVersion) {
        this.fileVersion = fileVersion;
    }

    public String getRetestResult() {
        return retestResult;
    }

    public void setRetestResult(String retestResult) {
        this.retestResult = retestResult;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
