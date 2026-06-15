package com.pvplatform.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "inverter")
public class Inverter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "roof_area_id")
    private Long roofAreaId;

    @Column(name = "inverter_code")
    private String inverterCode;

    @Column(name = "model")
    private String model;

    @Column(name = "capacity")
    private Double capacity;

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

    public String getInverterCode() {
        return inverterCode;
    }

    public void setInverterCode(String inverterCode) {
        this.inverterCode = inverterCode;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
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
