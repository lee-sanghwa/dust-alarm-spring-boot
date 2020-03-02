package com.dustalarm.scheduler;

import com.dustalarm.security.DustAlarmKeys;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.dustalarm.model.Station;
import com.dustalarm.service.DustAlarmService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component
public class StationScheduler {

    @Autowired
    private DustAlarmService dustAlarmService;
    private String airKoreaStationUrl = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getMsrstnList";
    private String airKoreaServiceKey = DustAlarmKeys.airKoreaServiceKey;
    private int numOfRows = 100;
    private String returnType = "json";

    @Scheduled(cron = "0 10 4 * * *")
    public void runStationScheduler() throws IOException {
        for (int pageNo = 1; pageNo < 7; pageNo++) {
            this.updateStation(pageNo);
            System.out.println("station update done with pageNo = " + pageNo);
        }
    }

    public void updateStation(int pageNo) throws IOException, NullPointerException {
        String airKoreaStationWithQueryStringUrl =
            airKoreaStationUrl +
                String.format("?serviceKey=%s", airKoreaServiceKey) +
                String.format("&numOfRows=%d", numOfRows) +
                String.format("&pageNo=%d", pageNo) +
                String.format("&_returnType=%s", returnType);

        URL airKoreaStationWithQueryStringURL = new URL(airKoreaStationWithQueryStringUrl);
        HttpURLConnection connection = (HttpURLConnection) airKoreaStationWithQueryStringURL.openConnection();
        connection.setRequestProperty("Accept", "application/json");
        connection.connect();

        BufferedReader tempBr = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String tempLine;
        while ((tempLine = tempBr.readLine()) != null) {
            sb.append(tempLine + "\n");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(sb.toString());
        tempBr.close();
        connection.disconnect();

        JsonNode jsonStationInfoList = jsonNode.get("list");

        for (JsonNode jsonStationInfo : jsonStationInfoList) {
            Map<String, Double> stationLocationMap = this.getLocationOfStation(jsonStationInfo);
            String stationName = jsonStationInfo.get("stationName").asText();
            String address = jsonStationInfo.get("addr").asText();

            Integer stationId;
            if (dustAlarmService.findStationByName(stationName) == null) {
                stationId = null;
            } else {
                stationId = dustAlarmService.findStationByName(stationName).getId();
            }
            dustAlarmService.saveStation(
                new Station(
                    stationId,
                    stationName,
                    address,
                    stationLocationMap.get("latitude"),
                    stationLocationMap.get("longitude"),
                    null,
                    null)
            );
        }
    }

    private Map<String, Double> getLocationOfStation(JsonNode jsonStationInfo) throws NullPointerException {
        Map<String, Double> stationLocationMap = new HashMap<>();

        try {
            stationLocationMap.put("latitude", jsonStationInfo.get("dmX").asDouble());
            stationLocationMap.put("longitude", jsonStationInfo.get("dmY").asDouble());
        } catch (NullPointerException e) {
            stationLocationMap.put("latitude", null);
            stationLocationMap.put("longitude", null);
        }

        return stationLocationMap;
    }
}
