package com.pvplatform.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pv_string")
public class PvString {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "roof_area_id")
    private Long roofAreaId;

    @Column(name = "string_code")
    private String stringCode;

    @Column(name = "panel_count")
    private Integer panelCount;

    @Column(name = "panel_model")
    private String panelModel;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "install_date")
    private Date installDate;

    @Column(name = "status")
    private Integer status;

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

    public String getStringCode() {
        return stringCode;
    }

    public void setStringCode(String stringCode) {
        this.stringCode = stringCode;
    }

    public Integer getPanelCount() {
        return panelCount;
    }

    public void setPanelCount(Integer panelCount) {
        this.panelCount = panelCount;
    }

    public String getPanelModel() {
        return panelModel;
    }

    public void setPanelModel(String panelModel) {
        this.panelModel = panelModel;
    }

    public Date getInstallDate() {
        return installDate;
    }

    public void setInstallDate(Date installDate) {
        this.installDate = installDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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
