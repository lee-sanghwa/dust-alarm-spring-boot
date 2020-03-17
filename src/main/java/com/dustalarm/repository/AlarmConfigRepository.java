package com.dustalarm.repository;

import com.dustalarm.model.AlarmConfig;

import java.util.Collection;

public interface AlarmConfigRepository {
    AlarmConfig findById(int id);

    Collection<AlarmConfig> findAll();

    Collection<AlarmConfig> findAll(Integer pageNo);

    void save(AlarmConfig alarmConfig);

    Integer findCount();
}
