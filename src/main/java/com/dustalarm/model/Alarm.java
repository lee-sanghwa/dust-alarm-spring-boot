package com.dustalarm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.dustalarm.rest.JacksonCustomAlarmSerializer;
import com.dustalarm.rest.JacksonCustomAlarmDeserializer;

import javax.persistence.*;

@Entity
@JsonDeserialize(using = JacksonCustomAlarmDeserializer.class)
@JsonSerialize(using = JacksonCustomAlarmSerializer.class)
@Table(name = "alarms_alarm")
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "time")
    private String time;

    @Column(name = "activated")
    private Boolean activated;

    @Column(name = "official_address")
    private String officialAddress;

    @JsonIgnore
    @Transient
    private String version;

    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "alarm_config_id")
    @JsonManagedReference(value = "alarm-alarmConfig")
    private AlarmConfig alarmConfig;

    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "station_id")
    @JsonManagedReference(value = "alarm-station")
    private Station station;

    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "user_id")
    @JsonManagedReference(value = "alarm-user")
    private User user;

    public Alarm() {
    }

    public Alarm(Integer id, String time, Boolean activated, String officialAddress, String version, AlarmConfig alarmConfig, Station station, User user) {
        this.id = id;
        this.time = time;
        this.activated = activated;
        this.officialAddress = officialAddress;
        this.version = version;
        this.alarmConfig = alarmConfig;
        this.station = station;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getOfficialAddress() {
        return officialAddress;
    }

    public void setOfficialAddress(String officialAddress) {
        this.officialAddress = officialAddress;
    }

    public AlarmConfig getAlarmConfig() {
        return alarmConfig;
    }

    public void setAlarmConfig(AlarmConfig alarmConfig) {
        this.alarmConfig = alarmConfig;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
