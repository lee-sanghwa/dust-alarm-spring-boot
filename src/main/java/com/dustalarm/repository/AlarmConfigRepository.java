package com.dustalarm.repository;

import org.springframework.dao.DataAccessException;
import com.dustalarm.model.AlarmConfig;

import java.util.Collection;

public interface AlarmConfigRepository {
    AlarmConfig findById(int id) throws DataAccessException;

    Collection<AlarmConfig> findAll() throws DataAccessException;

    Collection<AlarmConfig> findAll(Integer pageNo) throws DataAccessException;

    void save(AlarmConfig alarmConfig) throws DataAccessException;
}
