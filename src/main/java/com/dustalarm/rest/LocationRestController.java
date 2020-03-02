package com.dustalarm.rest;

import com.dustalarm.security.DustAlarmKeys;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.dustalarm.model.KakaoStation;
import com.dustalarm.model.Station;
import com.dustalarm.service.DustAlarmService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/api/locations")
public class LocationRestController {

    @Autowired
    private DustAlarmService dustAlarmService;
    private String kakaoGeocodingAddressUrl = "https://dapi.kakao.com/v2/local/search/address.json";
    private String kakaoGeocodingCoordUrl = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json";
    private String kakaoGeocodingKey = DustAlarmKeys.kakaoGeocodingKey;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getLocationResult(
        @RequestParam(value = "lat", required = false) Double latitude,
        @RequestParam(value = "lon", required = false) Double longitude,
        @RequestParam(value = "name", required = false) String name
    ) {
        if (latitude != null && longitude != null && name == null) {
            JsonNode jsonNodeForCoord = null;
            try {
                String kakaoGeocodinCoordWithLocationUrlString = this.kakaoGeocodingCoordUrl + String.format("?x=%s&y=%s", longitude, latitude);
                URL kakaoGeocodinCoordWithLocationURL = new URL(kakaoGeocodinCoordWithLocationUrlString);
                try {
                    HttpURLConnection connection = (HttpURLConnection) kakaoGeocodinCoordWithLocationURL.openConnection();
                    connection.setRequestProperty("Authorization", this.kakaoGeocodingKey);
                    connection.setRequestProperty("Accept", "application/json");
                    connection.connect();

                    BufferedReader tempBr = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String tempLine;
                    while ((tempLine = tempBr.readLine()) != null) {
                        sb.append(tempLine + "\n");
                    }

                    ObjectMapper mapper = new ObjectMapper();
                    jsonNodeForCoord = mapper.readTree(sb.toString());
                    tempBr.close();
                    connection.disconnect();
                } catch (IOException e) {
                    System.out.println("IOException : " + e);
                }
            } catch (MalformedURLException e) {
                System.out.println("MalformedURLException : " + e);
            }

            JsonNode jsonRegionList = jsonNodeForCoord.get("documents");

            if (jsonRegionList == null) {
                return new ResponseEntity<Collection<KakaoStation>>(Collections.EMPTY_LIST, HttpStatus.NOT_FOUND);
            } else {
                ArrayList<KakaoStation> stations = new ArrayList<KakaoStation>();

                for (JsonNode jsonRegion : jsonRegionList) {
                    Station station = dustAlarmService.findStationByLocation(jsonRegion.get("y").asDouble(), jsonRegion.get("x").asDouble());
                    stations.add(new KakaoStation(jsonRegion.get("address_name").toString().split("\"")[1], station.getId()));
                }
                return new ResponseEntity<KakaoStation>(stations.get(0), HttpStatus.OK);
            }
        } else if (latitude == null && longitude == null && name != null) {
            JsonNode jsonNodeForAddress = null;

            try {
                String kakaoGeocodingAddressWithQueryUrlString = this.kakaoGeocodingAddressUrl + String.format("?query=%s", URLEncoder.encode(name, "UTF-8"));
                URL kakaoGeocodingAddressWithQueryURL = new URL(kakaoGeocodingAddressWithQueryUrlString);
                try {
                    HttpURLConnection connection = (HttpURLConnection) kakaoGeocodingAddressWithQueryURL.openConnection();
                    connection.setRequestProperty("Authorization", this.kakaoGeocodingKey);
                    connection.setRequestProperty("Accept", "application/json");
                    connection.connect();

                    BufferedReader tempBr = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String tempLine;
                    while ((tempLine = tempBr.readLine()) != null) {
                        sb.append(tempLine + "\n");
                    }

                    ObjectMapper mapper = new ObjectMapper();
                    jsonNodeForAddress = mapper.readTree(sb.toString());

                    tempBr.close();
                    connection.disconnect();
                } catch (IOException e) {
                    System.out.println("IOException : " + e);
                }
            } catch (MalformedURLException | UnsupportedEncodingException e) {
                System.out.println("MalformedURLException | UnsupportedEncodingException : " + e);
            }

            JsonNode jsonAddressList = jsonNodeForAddress.get("documents");

            if (jsonAddressList.size() == 0) {
                return new ResponseEntity<Collection<KakaoStation>>(Collections.EMPTY_LIST, HttpStatus.NOT_FOUND);
            } else {
                ArrayList<KakaoStation> stations = new ArrayList<KakaoStation>();

                for (JsonNode jsonAddress : jsonAddressList) {
                    Station station = dustAlarmService.findStationByLocation(jsonAddress.get("y").asDouble(), jsonAddress.get("x").asDouble());
                    stations.add(new KakaoStation(jsonAddress.get("address_name").toString().split("\"")[1], station.getId()));
                }
                return new ResponseEntity<Collection<KakaoStation>>(stations, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<Collection<KakaoStation>>(HttpStatus.BAD_REQUEST);
        }
    }
}
