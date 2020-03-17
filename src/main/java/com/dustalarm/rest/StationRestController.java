package com.dustalarm.rest;

import com.dustalarm.common.DustAlarmCustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.dustalarm.model.Station;
import com.dustalarm.service.DustAlarmService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/api/stations")
public class StationRestController {

    @Autowired
    private DustAlarmService dustAlarmService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> handleStationRequest(
        HttpServletRequest request,
        @RequestParam(name = "page", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "lat", required = false) Double latitude,
        @RequestParam(value = "lon", required = false) Double longitude,
        @RequestParam(value = "name", required = false) String name
    ) {
        if (latitude != null && longitude != null && name == null) {
            Station station = this.dustAlarmService.findStationByLocation(latitude, longitude);
            return new ResponseEntity<>(station, HttpStatus.OK);
        } else if (latitude == null && longitude == null && name != null) {
            name = '%' + name + '%';
            Collection<Station> stations = this.dustAlarmService.findStationByNameLike(name);
            stations.addAll(this.dustAlarmService.findStationByAddressLike(name));
            return new ResponseEntity<>(stations, HttpStatus.OK);
        } else if (latitude == null && longitude == null && name == null) {
            Collection<Station> stations = this.dustAlarmService.findAllStations(pageNo);
            if (stations.isEmpty()) {
                return new ResponseEntity<Collection<Station>>(HttpStatus.NOT_FOUND);
            } else {

                return new ResponseEntity<>(new DustAlarmCustomResponse.Builder()
                    .count(this.dustAlarmService.findCountStations())
                    .results(stations)
                    .setNextPreviousUrl(request.getRequestURI(), pageNo)
                    .Build(), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<Collection<Station>>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Station> addStation(@RequestBody @Valid Station station,
                                              BindingResult bindingResult,
                                              UriComponentsBuilder ucBuilder) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (station == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<Station>(headers, HttpStatus.BAD_REQUEST);
        } else {
            this.dustAlarmService.saveStation(station);
            headers.setLocation(ucBuilder.path("api/stations/{id}").buildAndExpand(station.getId()).toUri());
            return new ResponseEntity<Station>(station, headers, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/{stationId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Station> getStation(
        @PathVariable("stationId") int stationId
    ) {
        Station station = this.dustAlarmService.findStationById(stationId);
        if (station == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(station, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{stationId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Station> updateStation(@PathVariable("stationId") int stationId,
                                                 @RequestBody @Valid Station station,
                                                 BindingResult bindingResult,
                                                 UriComponentsBuilder ucBuilder) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (station == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<Station>(headers, HttpStatus.BAD_REQUEST);
        } else {
            Station currentStation = this.dustAlarmService.findStationById(stationId);
            if (currentStation == null) {
                return new ResponseEntity<Station>(HttpStatus.NOT_FOUND);
            } else {
                currentStation.setName(station.getName());
                currentStation.setAddress(station.getAddress());
                currentStation.setLatitude(station.getLatitude());
                currentStation.setLongitude(station.getLongitude());
                this.dustAlarmService.saveStation(currentStation);
                headers.setLocation(ucBuilder.path("api/stations/{id}").buildAndExpand(currentStation.getId()).toUri());
                return new ResponseEntity<Station>(currentStation, headers, HttpStatus.OK);
            }
        }
    }
}
