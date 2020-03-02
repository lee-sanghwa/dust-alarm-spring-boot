package com.dustalarm.service;

import org.springframework.dao.DataAccessException;
import com.dustalarm.model.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface DustAlarmService {
    Station findStationById(int id) throws DataAccessException;

    Station findStationByLocation(Double latitude, Double longitude) throws DataAccessException;

    Station findStationByName(String name) throws DataAccessException;

    Collection<Station> findStationByNameLike(String name) throws DataAccessException;

    Collection<Station> findStationByAddressLike(String address) throws DataAccessException;

    @Transactional
    void saveStation(Station station) throws DataAccessException;

    Collection<Station> findAllStations() throws DataAccessException;

    Collection<Station> findAllStations(Integer pageNo) throws DataAccessException;

    User findUserById(int id) throws DataAccessException;

    User findUserByUuid(String uuid) throws DataAccessException;

    @Transactional
    void saveUser(User user) throws DataAccessException;

    Collection<User> findAllUsers() throws DataAccessException;

    Collection<User> findAllUsers(Integer pageNo) throws DataAccessException;

    Concentration findConcentrationById(int id) throws DataAccessException;

    Concentration findConcentrationByStationId(int stationId) throws DataAccessException;

    @Transactional
    void saveConcentration(Concentration concentration) throws DataAccessException;

    Collection<Concentration> findAllConcentrations() throws DataAccessException;

    Collection<Concentration> findAllConcentrations(Integer pageNo) throws DataAccessException;

    AlarmConfig findAlarmConfigById(int id) throws DataAccessException;

    Collection<AlarmConfig> findAllAlarmConfigs() throws DataAccessException;

    Collection<AlarmConfig> findAllAlarmConfigs(Integer pageNo) throws DataAccessException;

    @Transactional
    void saveAlarmConfig(AlarmConfig alarmConfig) throws DataAccessException;

    Alarm findAlarmById(int id) throws DataAccessException;

    Collection<Alarm> findAlarmByTimeActivated(String time) throws DataAccessException;

    Collection<Alarm> findAlarmByUserId(int userId) throws DataAccessException;

    Collection<Alarm> findAllAlarms() throws DataAccessException;

    Collection<Alarm> findAllAlarms(Integer pageNo) throws DataAccessException;

    @Transactional
    void saveAlarm(Alarm alarm) throws DataAccessException;

    ForecastConcentration findFCByRegionName(String regionName) throws DataAccessException;

    @Transactional
    void saveFC(ForecastConcentration fC) throws DataAccessException;
}
