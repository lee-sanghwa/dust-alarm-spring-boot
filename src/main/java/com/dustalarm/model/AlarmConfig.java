package com.dustalarm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "alarmconfigs_alarmconfig")
public class AlarmConfig {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "week")
    private String week;

    @Column(name = "when")
    private String when;

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        mappedBy = "alarmConfig")
    @JsonBackReference(value = "alarmConfig-alarms")
    private Set<Alarm> alarms;

    public AlarmConfig() {
    }

    public AlarmConfig(Integer id, String week, String when, Set<Alarm> alarms) {
        this.id = id;
        this.week = week;
        this.when = when;
        this.alarms = alarms;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public Set<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(Set<Alarm> alarms) {
        this.alarms = alarms;
    }
}
