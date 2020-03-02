package com.dustalarm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ForecastConcentration")
public class ForecastConcentration {
    @Id
    @Column(name = "region_name")
    private String regionName;

    @Column(name = "announcement_time")
    private String announcementTime;

    @Column(name = "target_date")
    private String targetDate;

    @Column(name = "fine_dust_grade")
    private Integer fineDustGrade;

    @Column(name = "ultra_fine_dust_grade")
    private Integer ultraFineDustGrade;

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "fc")
    @JsonBackReference(value = "forecastConcentration-concentrations")
    private Set<Concentration> concentrations;

    public ForecastConcentration() {
    }

    public ForecastConcentration(
        String regionName,
        String announcementTime,
        String targetDate,
        Integer fineDustGrade,
        Integer ultraFineDustGrade
    ) {
        this.regionName = regionName;
        this.announcementTime = announcementTime;
        this.targetDate = targetDate;
        this.fineDustGrade = fineDustGrade;
        this.ultraFineDustGrade = ultraFineDustGrade;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getAnnouncementTime() {
        return announcementTime;
    }

    public void setAnnouncementTime(String announcementTime) {
        this.announcementTime = announcementTime;
    }

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    public Integer getFineDustGrade() {
        return fineDustGrade;
    }

    public void setFineDustGrade(Integer fineDustGrade) {
        this.fineDustGrade = fineDustGrade;
    }

    public Integer getUltraFineDustGrade() {
        return ultraFineDustGrade;
    }

    public void setUltraFineDustGrade(Integer ultraFineDustGrade) {
        this.ultraFineDustGrade = ultraFineDustGrade;
    }
}
