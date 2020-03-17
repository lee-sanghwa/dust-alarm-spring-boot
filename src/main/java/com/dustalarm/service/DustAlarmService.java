package com.dustalarm.service;

import com.dustalarm.model.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface DustAlarmService {
    Station findStationById(int id);

    Station findStationByLocation(Double latitude, Double longitude);

    Station findStationByName(String name);

    Collection<Station> findStationByNameLike(String name);

    Collection<Station> findStationByAddressLike(String address);

    @Transactional
    void saveStation(Station station);

    Collection<Station> findAllStations();

    Collection<Station> findAllStations(Integer pageNo);

    Integer findCountStations();

    User findUserById(int id);

    User findUserByUuid(String uuid);

    @Transactional
    void saveUser(User user);

    Collection<User> findAllUsers();

    Collection<User> findAllUsers(Integer pageNo);

    Integer findCountUsers();

    Concentration findConcentrationById(int id);

    Concentration findConcentrationByStationId(int stationId);

    @Transactional
    void saveConcentration(Concentration concentration);

    Collection<Concentration> findAllConcentrations();

    Collection<Concentration> findAllConcentrations(Integer pageNo);

    Integer findCountConcentrations();

    AlarmConfig findAlarmConfigById(int id);

    Collection<AlarmConfig> findAllAlarmConfigs();

    Collection<AlarmConfig> findAllAlarmConfigs(Integer pageNo);

    @Transactional
    void saveAlarmConfig(AlarmConfig alarmConfig);

    Integer findCountAlarmConfigs();

    Alarm findAlarmById(int id);

    Collection<Alarm> findAlarmByTimeActivated(String time);

    Collection<Alarm> findAlarmByUserId(int userId);

    Collection<Alarm> findAllAlarms();

    Collection<Alarm> findAllAlarms(Integer pageNo);

    @Transactional
    void saveAlarm(Alarm alarm);

    Integer findCountAlarms();

    ForecastConcentration findFCByRegionName(String regionName);

    @Transactional
    void saveFC(ForecastConcentration fC);
}
