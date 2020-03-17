package com.dustalarm.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    public Station findStationById(int id) {
        return stationRepository.findById(id);
    }

    public Station findStationByLocation(Double latitude, Double longitude) {
        return stationRepository.findByLocation(latitude, longitude);
    }

    public Station findStationByName(String name) {
        return stationRepository.findByName(name);
    }

    public Collection<Station> findStationByNameLike(String name) {
        return stationRepository.findByNameLike(name);
    }

    public Collection<Station> findStationByAddressLike(String address) {
        return stationRepository.findByAddressLike(address);
    }

    public void saveStation(Station station) {
        stationRepository.save(station);
    }

    public Collection<Station> findAllStations() {
        return stationRepository.findAll();
    }

    public Collection<Station> findAllStations(Integer pageNo) {
        return stationRepository.findAll(pageNo);
    }

    public Integer findCountStations() {
        return stationRepository.findCount();
    }

    public User findUserById(int id) {
        return userRepository.findById(id);
    }

    public User findUserByUuid(String uuid) {
        return userRepository.findByUuid(uuid);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Collection<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Collection<User> findAllUsers(Integer pageNo) {
        return userRepository.findAll(pageNo);
    }

    public Integer findCountUsers() {
        return userRepository.findCount();
    }

    public Concentration findConcentrationById(int id) {
        return concentrationRepository.findById(id);
    }

    public Concentration findConcentrationByStationId(int stationId) {
        return concentrationRepository.findByStationId(stationId);
    }


    public void saveConcentration(Concentration concentration) {
        concentrationRepository.save(concentration);
    }

    public Collection<Concentration> findAllConcentrations() {
        return concentrationRepository.findAll();
    }

    public Collection<Concentration> findAllConcentrations(Integer pageNo) {
        return concentrationRepository.findAll(pageNo);
    }

    public Integer findCountConcentrations() {
        return concentrationRepository.findCount();
    }

    public AlarmConfig findAlarmConfigById(int id) {
        return alarmConfigRepository.findById(id);
    }

    public void saveAlarmConfig(AlarmConfig alarmConfig) {
        alarmConfigRepository.save(alarmConfig);
    }

    public Collection<AlarmConfig> findAllAlarmConfigs() {
        return alarmConfigRepository.findAll();
    }

    public Collection<AlarmConfig> findAllAlarmConfigs(Integer pageNo) {
        return alarmConfigRepository.findAll(pageNo);
    }

    public Integer findCountAlarmConfigs() {
        return alarmConfigRepository.findCount();
    }

    public Alarm findAlarmById(int id) {
        return alarmRepository.findById(id);
    }

    public Collection<Alarm> findAlarmByTimeActivated(String time) {
        return alarmRepository.findByTimeActivated(time);
    }

    public Collection<Alarm> findAlarmByUserId(int userId) {
        return alarmRepository.findByUserId(userId);
    }

    public Collection<Alarm> findAllAlarms() {
        return alarmRepository.findAll();
    }

    public Collection<Alarm> findAllAlarms(Integer pageNo) {
        return alarmRepository.findAll(pageNo);
    }

    public void saveAlarm(Alarm alarm) {
        alarmRepository.save(alarm);
    }

    public Integer findCountAlarms() {
        return alarmRepository.findCount();
    }

    public ForecastConcentration findFCByRegionName(String regionName) {
        return forecastConcentrationRepository.findByRegionName(regionName);
    }

    public void saveFC(ForecastConcentration fC) {
        forecastConcentrationRepository.save(fC);
    }

}
