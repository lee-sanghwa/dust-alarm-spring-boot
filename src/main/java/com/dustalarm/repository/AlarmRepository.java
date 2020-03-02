package com.dustalarm.repository;

import org.springframework.dao.DataAccessException;
import com.dustalarm.model.Alarm;

import java.util.Collection;

public interface AlarmRepository {
    Alarm findById(int id) throws DataAccessException;

    Collection<Alarm> findByUserId(int userId) throws DataAccessException;

    Collection<Alarm> findByTimeActivated(String time) throws DataAccessException;

    Collection<Alarm> findAll() throws DataAccessException;

    Collection<Alarm> findAll(Integer pageNo) throws DataAccessException;

    void save(Alarm alarm) throws DataAccessException;

}
