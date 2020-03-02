package com.dustalarm.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.dustalarm.rest.JacksonCustomConcentrationSerializer;

import javax.persistence.*;

@Entity
@JsonSerialize(using = JacksonCustomConcentrationSerializer.class)
@Table(name = "concentrations_concentration")
public class Concentration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fine_dust")
    private Integer fineDust;

    @Column(name = "fine_dust_grade")
    private Integer fineDustGrade;

    @Column(name = "ultra_fine_dust")
    private Integer ultraFineDust;

    @Column(name = "ultra_fine_dust_grade")
    private Integer ultraFineDustGrade;

    @Column(name = "data_time")
    private String dataTime;

    @OneToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "station_id", nullable = false)
    @JsonManagedReference(value = "concentration-station")
    private Station station;

    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "forecast_concentration_region_name")
    @JsonManagedReference(value = "concentration-forecastConcentration")
    private ForecastConcentration fc;

    public Concentration() {
    }

    public Concentration(
        Integer id,
        Integer fineDust,
        Integer fineDustGrade,
        Integer ultraFineDust,
        Integer ultraFineDustGrade,
        String dataTime,
        Station station,
        ForecastConcentration fc
    ) {
        this.id = id;
        this.fineDust = fineDust;
        this.fineDustGrade = fineDustGrade;
        this.ultraFineDust = ultraFineDust;
        this.ultraFineDustGrade = ultraFineDustGrade;
        this.dataTime = dataTime;
        this.station = station;
        this.fc = fc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFineDust() {
        return id;
    }

    public void setFineDust(Integer fineDust) {
        this.fineDust = fineDust;
    }

    public Integer getFineDustGrade() {
        return fineDustGrade;
    }

    public void setFineDustGrade(Integer fineDustGrade) {
        this.fineDustGrade = fineDustGrade;
    }

    public Integer getUltraFineDust() {
        return ultraFineDust;
    }

    public void setUltraFineDust(Integer ultraFineDust) {
        this.ultraFineDust = ultraFineDust;
    }

    public Integer getUltraFineDustGrade() {
        return ultraFineDustGrade;
    }

    public void setUltraFineDustGrade(Integer ultraFineDustGrade) {
        this.ultraFineDustGrade = ultraFineDustGrade;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dateTime) {
        this.dataTime = dateTime;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public ForecastConcentration getFc() {
        return fc;
    }

    public void setFc(ForecastConcentration fc) {
        this.fc = fc;
    }
}
