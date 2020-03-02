package com.dustalarm.repository;

import org.springframework.dao.DataAccessException;
import com.dustalarm.model.ForecastConcentration;

public interface ForecastConcentrationRepository {
    ForecastConcentration findByRegionName(String regionName) throws DataAccessException;

    void save(ForecastConcentration forecastConcentration) throws DataAccessException;
}
