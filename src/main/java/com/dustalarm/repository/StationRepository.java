package com.dustalarm.repository;

import org.springframework.dao.DataAccessException;
import com.dustalarm.model.Station;

import java.util.Collection;

public interface StationRepository {
    Station findById(int id) throws DataAccessException;

    Station findByLocation(Double latitude, Double longitude) throws DataAccessException;

    Station findByName(String name) throws DataAccessException;

    Collection<Station> findByNameLike(String name) throws DataAccessException;

    Collection<Station> findByAddressLike(String address) throws DataAccessException;

    void save(Station station) throws DataAccessException;

    Collection<Station> findAll() throws DataAccessException;

    Collection<Station> findAll(Integer pageNo) throws DataAccessException;
}
