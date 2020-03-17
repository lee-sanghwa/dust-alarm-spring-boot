package com.dustalarm.rest;

import com.dustalarm.common.DustAlarmCustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.dustalarm.model.AlarmConfig;
import com.dustalarm.service.DustAlarmService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/alarmconfigs")
public class AlarmConfigRestController {

    @Autowired
    private DustAlarmService dustAlarmService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAlarmConfigs(
        HttpServletRequest request,
        @RequestParam(name = "page", defaultValue = "1") Integer pageNo
    ) {
        return new ResponseEntity<>(
            new DustAlarmCustomResponse.Builder()
                .count(this.dustAlarmService.findCountAlarmConfigs())
                .results(this.dustAlarmService.findAllAlarmConfigs(pageNo))
                .setNextPreviousUrl(request.getRequestURI(), pageNo)
                .Build(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{alarmConfigId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AlarmConfig> getAlarmConfig(
        @PathVariable(value = "alarmConfigId", required = false) int alarmConfigId
    ) {
        AlarmConfig alarmConfig = dustAlarmService.findAlarmConfigById(alarmConfigId);
        return new ResponseEntity<>(alarmConfig, HttpStatus.OK);
    }
}
