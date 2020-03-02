package com.dustalarm.repository;

import org.springframework.dao.DataAccessException;
import com.dustalarm.model.Concentration;

import java.util.Collection;

public interface ConcentrationRepository {
    Concentration findById(int id) throws DataAccessException;

    Concentration findByStationId(int stationId) throws DataAccessException;

    void save(Concentration concentration) throws DataAccessException;

    Collection<Concentration> findAll() throws DataAccessException;

    Collection<Concentration> findAll(Integer pageNo) throws DataAccessException;
}
