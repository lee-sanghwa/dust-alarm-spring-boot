package com.dustalarm.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.dustalarm.model.AlarmConfig;
import com.dustalarm.service.DustAlarmService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping("/api/alarmconfigs")
public class AlarmConfigRestController {

    @Autowired
    private DustAlarmService dustAlarmService;

    @RequestMapping(value = "/{alarmConfigId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AlarmConfig> getAlarmConfig(@PathVariable(value = "alarmConfigId", required = false) int alarmConfigId) {
        AlarmConfig alarmConfig = dustAlarmService.findAlarmConfigById(alarmConfigId);
        return new ResponseEntity<AlarmConfig>(alarmConfig, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAlarmConfigs(
        HttpServletRequest request,
        @RequestParam(name = "page", defaultValue = "1") Integer pageNo
    ) {
        Collection<AlarmConfig> totalAlarmConfigs = this.dustAlarmService.findAllAlarmConfigs();
        Collection<AlarmConfig> alarmConfigs = this.dustAlarmService.findAllAlarmConfigs(pageNo);

        DustAlarmCustomResponse response = new DustAlarmCustomResponse(totalAlarmConfigs, alarmConfigs);
        response.setNextPreviousUrl(request, pageNo, totalAlarmConfigs);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
