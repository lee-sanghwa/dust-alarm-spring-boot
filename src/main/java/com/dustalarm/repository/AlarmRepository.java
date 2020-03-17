package com.dustalarm.repository;

import com.dustalarm.model.Alarm;

import java.util.Collection;

public interface AlarmRepository {
    Alarm findById(int id);

    Collection<Alarm> findByUserId(int userId);

    Collection<Alarm> findByTimeActivated(String time);

    Collection<Alarm> findAll();

    Collection<Alarm> findAll(Integer pageNo);

    void save(Alarm alarm);

    Integer findCount();

}
