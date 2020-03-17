package com.dustalarm.scheduler;

import com.dustalarm.common.DustAlarmCommon;
import com.dustalarm.security.DustAlarmKeys;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import com.dustalarm.model.Station;
import com.dustalarm.service.DustAlarmService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
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

        Map<String, String> headerConfig = new HashMap<>();
        headerConfig.put("Accept", "application/json");

        JsonNode responseBody = DustAlarmCommon.getResponseBodyFromUrl(airKoreaStationWithQueryStringUrl, headerConfig);

        JsonNode jsonStationInfoList = responseBody.get("list");

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
