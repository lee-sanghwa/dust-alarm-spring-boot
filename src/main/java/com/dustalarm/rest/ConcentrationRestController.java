package com.dustalarm.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.dustalarm.model.Concentration;
import com.dustalarm.service.DustAlarmService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/api/concentrations")
public class ConcentrationRestController {

    @Autowired
    DustAlarmService dustAlarmService;

    @RequestMapping(value = "/{concentrationId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Concentration> findById(@PathVariable(name = "concentrationId") int concentrationId) {
        Concentration concentration = dustAlarmService.findConcentrationById(concentrationId);
        if (concentration == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Concentration>(concentration, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> handleConcentrationRequest(
        HttpServletRequest request,
        @RequestParam(name = "station", required = false) Integer stationId,
        @RequestParam(name = "page", defaultValue = "1") Integer pageNo
    ) {
        if (stationId == null) {
            Collection<Concentration> totalConcentrations = dustAlarmService.findAllConcentrations();
            Collection<Concentration> concentrations = dustAlarmService.findAllConcentrations(pageNo);


            DustAlarmCustomResponse response = new DustAlarmCustomResponse(totalConcentrations, concentrations);
            response.setNextPreviousUrl(request, pageNo, totalConcentrations);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Concentration concentration = dustAlarmService.findConcentrationByStationId(stationId.intValue());
            if (concentration == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(concentration, HttpStatus.OK);
            }
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Concentration> addConcentration(
        @RequestBody @Valid Concentration concentration,
        BindingResult bindingResult,
        UriComponentsBuilder ucBuilder
    ) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (concentration == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<Concentration>(headers, HttpStatus.BAD_REQUEST);
        } else {
            this.dustAlarmService.saveConcentration(concentration);
            headers.setLocation(ucBuilder.path("api/stations/{id}").buildAndExpand(concentration.getId()).toUri());
            return new ResponseEntity<Concentration>(concentration, headers, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/{concentrationId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Concentration> updateConcentration(
        @PathVariable(value = "concentrationId", required = false) Integer concentrationId,
        @RequestBody @Valid Concentration concentration,
        BindingResult bindingResult,
        UriComponentsBuilder ucBuilder
    ) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (concentration == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<Concentration>(headers, HttpStatus.BAD_REQUEST);
        } else {
            Concentration currentConcentration = this.dustAlarmService.findConcentrationById(concentrationId);
            if (currentConcentration == null) {
                return new ResponseEntity<Concentration>(HttpStatus.NOT_FOUND);
            } else {
                currentConcentration.setFineDust(concentration.getFineDust());
                currentConcentration.setFineDustGrade(concentration.getFineDustGrade());
                currentConcentration.setUltraFineDust(concentration.getUltraFineDust());
                currentConcentration.setUltraFineDustGrade(concentration.getUltraFineDustGrade());
                currentConcentration.setDataTime(concentration.getDataTime());
                this.dustAlarmService.saveConcentration(currentConcentration);
                headers.setLocation(ucBuilder.path("api/concentrations/{id}").buildAndExpand(currentConcentration.getId()).toUri());
                return new ResponseEntity<Concentration>(currentConcentration, headers, HttpStatus.OK);
            }
        }
    }
}
