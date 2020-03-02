package com.dustalarm.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class KakaoStation {
    @Id
    private String address_name;

    private Integer station;

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public Integer getStation() {
        return station;
    }

    public void setStation(Integer station) {
        this.station = station;
    }

    public KakaoStation() {
    }

    public KakaoStation(String address_name, Integer station) {
        this.address_name = address_name;
        this.station = station;
    }
}
