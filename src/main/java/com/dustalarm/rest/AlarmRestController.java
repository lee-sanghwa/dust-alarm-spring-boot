package com.dustalarm.rest;

import com.dustalarm.common.DustAlarmCommon;
import com.dustalarm.common.DustAlarmCustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.dustalarm.model.*;
import com.dustalarm.service.DustAlarmService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.dustalarm.common.DustAlarmCommon.setUserAgent2Alarms;

@RestController
@RequestMapping("/api/alarms")
public class AlarmRestController {

    @Autowired
    private DustAlarmService dustAlarmService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAlarms(
        HttpServletRequest request,
        @RequestParam(name = "user", required = false) Integer userId,
        @RequestParam(name = "page", defaultValue = "1") Integer pageNo,
        @RequestHeader(value = "User-Agent", required = false) String userAgent
    ) {
        if (userId == null) {
            /* Return All of Alarms of Database */

            Collection<Alarm> alarms = dustAlarmService.findAllAlarms(pageNo);
            setUserAgent2Alarms(alarms, userAgent);

            return new ResponseEntity<>(new DustAlarmCustomResponse.Builder()
                .count(this.dustAlarmService.findCountAlarms())
                .results(alarms)
                .setNextPreviousUrl(request.getRequestURI(), pageNo)
                .Build(), HttpStatus.OK);
        } else {

            /* Return All of Alarms Of Specific User */
            Collection<Alarm> alarms = dustAlarmService.findAlarmByUserId(userId);
            setUserAgent2Alarms(alarms, userAgent);

            if (!"2.0.0".equals(userAgent)) {

                /* Return All of Alarms Of Specific User With Version 1.0.0 */
                alarms = this.setAlarms4Ver1(new ArrayList<>(alarms), userId);
            } else {
            }

            return new ResponseEntity<>(new DustAlarmCustomResponse.Builder()
                .count(alarms.size())
                .results(alarms)
                .setNextPreviousUrl(request.getRequestURI(), pageNo)
                .Build(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Alarm> save(
        @RequestHeader(value = "User-Agent", required = false) String userAgent,
        @RequestBody @Valid Alarm alarm,
        BindingResult bindingResult,
        UriComponentsBuilder ucBuilder
    ) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (alarm == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<Alarm>(headers, HttpStatus.BAD_REQUEST);
        } else {
            alarm = DustAlarmCommon.setUserAgent(alarm, userAgent);
            this.dustAlarmService.saveAlarm(alarm);
            headers.setLocation(ucBuilder.path("api/alarms/{id}").buildAndExpand(alarm.getId()).toUri());
            return new ResponseEntity<Alarm>(alarm, headers, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/{alarmId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Alarm> getAlarm(
        @PathVariable(value = "alarmId", required = false) int alarmId,
        @RequestHeader(value = "User-Agent", required = false) String userAgent
    ) {
        Alarm alarm = dustAlarmService.findAlarmById(alarmId);
        alarm = DustAlarmCommon.setUserAgent(alarm, userAgent);
        return new ResponseEntity<Alarm>(alarm, HttpStatus.OK);
    }


    @RequestMapping(value = "/{alarmId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateConcentration(
        @PathVariable(value = "alarmId", required = false) Integer alarmId,
        @RequestHeader(value = "User-Agent", required = false) String userAgent,
        @RequestBody @Valid Alarm alarm,
        BindingResult bindingResult,
        UriComponentsBuilder ucBuilder
    ) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (alarm == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<Concentration>(headers, HttpStatus.BAD_REQUEST);
        } else {
            Alarm currentAlarm = this.dustAlarmService.findAlarmById(alarmId);
            if (currentAlarm == null) {
                return new ResponseEntity<Station>(HttpStatus.NOT_FOUND);
            } else {
                currentAlarm.setActivated(alarm.getActivated());
                currentAlarm.setAlarmConfig(alarm.getAlarmConfig());
                currentAlarm.setOfficialAddress(alarm.getOfficialAddress());
                currentAlarm.setStation(alarm.getStation());
                currentAlarm.setTime(alarm.getTime());

                currentAlarm = DustAlarmCommon.setUserAgent(currentAlarm, userAgent);

                this.dustAlarmService.saveAlarm(currentAlarm);
                headers.setLocation(ucBuilder.path("api/alarms/{id}").buildAndExpand(currentAlarm.getId()).toUri());
                return new ResponseEntity<Alarm>(currentAlarm, headers, HttpStatus.OK);
            }
        }
    }

    private Collection<Alarm> setAlarms4Ver1(List<Alarm> alarms, Integer userId) {
        User user = dustAlarmService.findUserById(userId);
        List<Alarm> resetAlarms = new ArrayList<>();

        for (int alarmConfigId = 1; alarmConfigId < 5; alarmConfigId++) {
            Alarm tempAlarm = new Alarm(
                0,
                "00:00",
                false,
                null,
                null,
                dustAlarmService.findAlarmConfigById(alarmConfigId),
                null,
                user);

            resetAlarms.add(tempAlarm);
        }

        for (Alarm alarm : alarms) {
            int currentAlarmConfigId = alarm.getAlarmConfig().getId();
            if (currentAlarmConfigId > 0 && currentAlarmConfigId < 5) {
                // index는 0부터 시작 but alarmConfigId는 1부터 시작
                resetAlarms.get(currentAlarmConfigId - 1).setId(alarm.getId());
                resetAlarms.get(currentAlarmConfigId - 1).setTime(alarm.getTime());
                resetAlarms.get(currentAlarmConfigId - 1).setStation(alarm.getStation());
                resetAlarms.get(currentAlarmConfigId - 1).setActivated(alarm.getActivated());
                resetAlarms.get(currentAlarmConfigId - 1).setVersion(alarm.getVersion());
            }
        }
        return resetAlarms;
    }
}
