package com.pvplatform.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "meter_reading")
public class MeterReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "inverter_id")
    private Long inverterId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reading_date")
    private Date readingDate;

    @Column(name = "generation")
    private Double generation;

    @Column(name = "meter_reading")
    private Double meterReading;

    @Column(name = "reading_by")
    private Long readingBy;

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

    public Date getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(Date readingDate) {
        this.readingDate = readingDate;
    }

    public Double getGeneration() {
        return generation;
    }

    public void setGeneration(Double generation) {
        this.generation = generation;
    }

    public Double getMeterReading() {
        return meterReading;
    }

    public void setMeterReading(Double meterReading) {
        this.meterReading = meterReading;
    }

    public Long getReadingBy() {
        return readingBy;
    }

    public void setReadingBy(Long readingBy) {
        this.readingBy = readingBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
