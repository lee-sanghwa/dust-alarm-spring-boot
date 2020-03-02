package com.dustalarm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import com.dustalarm.model.*;
import com.dustalarm.repository.*;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DustAlarmServiceImpl implements DustAlarmService {

    private StationRepository stationRepository;
    private UserRepository userRepository;
    private ConcentrationRepository concentrationRepository;
    private AlarmConfigRepository alarmConfigRepository;
    private AlarmRepository alarmRepository;
    private ForecastConcentrationRepository forecastConcentrationRepository;

    @Autowired
    public DustAlarmServiceImpl(
        StationRepository stationRepository,
        UserRepository userRepository,
        ConcentrationRepository concentrationRepository,
        AlarmConfigRepository alarmConfigRepository,
        AlarmRepository alarmRepository,
        ForecastConcentrationRepository forecastConcentrationRepository
    ) {
        this.stationRepository = stationRepository;
        this.userRepository = userRepository;
        this.concentrationRepository = concentrationRepository;
        this.alarmConfigRepository = alarmConfigRepository;
        this.alarmRepository = alarmRepository;
        this.forecastConcentrationRepository = forecastConcentrationRepository;
    }

    public Station findStationById(int id) throws DataAccessException {
        return stationRepository.findById(id);
    }

    public Station findStationByLocation(Double latitude, Double longitude) throws DataAccessException {
        return stationRepository.findByLocation(latitude, longitude);
    }

    public Station findStationByName(String name) throws DataAccessException {
        return stationRepository.findByName(name);
    }

    public Collection<Station> findStationByNameLike(String name) throws DataAccessException {
        return stationRepository.findByNameLike(name);
    }

    public Collection<Station> findStationByAddressLike(String address) throws DataAccessException {
        return stationRepository.findByAddressLike(address);
    }

    public void saveStation(Station station) throws DataAccessException {
        stationRepository.save(station);
    }

    public Collection<Station> findAllStations() throws DataAccessException {
        return stationRepository.findAll();
    }

    public Collection<Station> findAllStations(Integer pageNo) throws DataAccessException {
        return stationRepository.findAll(pageNo);
    }

    public User findUserById(int id) throws DataAccessException {
        return userRepository.findById(id);
    }

    public User findUserByUuid(String uuid) throws DataAccessException {
        return userRepository.findByUuid(uuid);
    }

    public void saveUser(User user) throws DataAccessException {
        userRepository.save(user);
    }

    public Collection<User> findAllUsers() throws DataAccessException {
        return userRepository.findAll();
    }

    public Collection<User> findAllUsers(Integer pageNo) throws DataAccessException {
        return userRepository.findAll(pageNo);
    }

    public Concentration findConcentrationById(int id) throws DataAccessException {
        return concentrationRepository.findById(id);
    }

    public Concentration findConcentrationByStationId(int stationId) throws DataAccessException {
        return concentrationRepository.findByStationId(stationId);
    }


    public void saveConcentration(Concentration concentration) throws DataAccessException {
        concentrationRepository.save(concentration);
    }

    public Collection<Concentration> findAllConcentrations() throws DataAccessException {
        return concentrationRepository.findAll();
    }

    public Collection<Concentration> findAllConcentrations(Integer pageNo) throws DataAccessException {
        return concentrationRepository.findAll(pageNo);
    }

    public AlarmConfig findAlarmConfigById(int id) throws DataAccessException {
        return alarmConfigRepository.findById(id);
    }

    public void saveAlarmConfig(AlarmConfig alarmConfig) throws DataAccessException {
        alarmConfigRepository.save(alarmConfig);
    }

    public Collection<AlarmConfig> findAllAlarmConfigs() throws DataAccessException {
        return alarmConfigRepository.findAll();
    }

    public Collection<AlarmConfig> findAllAlarmConfigs(Integer pageNo) throws DataAccessException {
        return alarmConfigRepository.findAll(pageNo);
    }

    public Alarm findAlarmById(int id) throws DataAccessException {
        return alarmRepository.findById(id);
    }

    public Collection<Alarm> findAlarmByTimeActivated(String time) throws DataAccessException {
        return alarmRepository.findByTimeActivated(time);
    }

    public Collection<Alarm> findAlarmByUserId(int userId) throws DataAccessException {
        return alarmRepository.findByUserId(userId);
    }

    public Collection<Alarm> findAllAlarms() throws DataAccessException {
        return alarmRepository.findAll();
    }

    public Collection<Alarm> findAllAlarms(Integer pageNo) throws DataAccessException {
        return alarmRepository.findAll(pageNo);
    }

    public void saveAlarm(Alarm alarm) throws DataAccessException {
        alarmRepository.save(alarm);
    }

    public ForecastConcentration findFCByRegionName(String regionName) throws DataAccessException {
        return forecastConcentrationRepository.findByRegionName(regionName);
    }

    public void saveFC(ForecastConcentration fC) throws DataAccessException {
        forecastConcentrationRepository.save(fC);
    }

}
