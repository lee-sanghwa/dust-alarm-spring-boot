package com.dustalarm.repository;

import com.dustalarm.model.Station;

import java.util.Collection;

public interface StationRepository {
    Station findById(int id);

    Station findByLocation(Double latitude, Double longitude);

    Station findByName(String name);

    Collection<Station> findByNameLike(String name);

    Collection<Station> findByAddressLike(String address);

    void save(Station station);

    Collection<Station> findAll();

    Collection<Station> findAll(Integer pageNo);

    Integer findCount();
}
