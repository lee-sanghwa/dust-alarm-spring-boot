package com.dustalarm.repository;

import com.dustalarm.model.Concentration;

import java.util.Collection;

public interface ConcentrationRepository {
    Concentration findById(int id);

    Concentration findByStationId(int stationId);

    void save(Concentration concentration);

    Collection<Concentration> findAll();

    Collection<Concentration> findAll(Integer pageNo);

    Integer findCount();
}
