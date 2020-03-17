package com.dustalarm.scheduler;

import com.dustalarm.common.DustAlarmCommon;
import com.dustalarm.security.DustAlarmKeys;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import com.dustalarm.model.Concentration;
import com.dustalarm.model.ForecastConcentration;
import com.dustalarm.model.Station;
import com.dustalarm.service.DustAlarmService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class DustScheduler {

    @Autowired
    private DustAlarmService dustAlarmService;
    private String airKoreaDustUrl = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
    private String airKoreaServiceKey = DustAlarmKeys.airKoreaServiceKey;
    private int numOfRows = 150;
    private int pageNo = 1;
    private Double version = 1.3;
    private String returnType = "json";

    @Scheduled(cron = "0 */3 * * * *")
    public void runDustScheduler() throws IOException, NullPointerException {
        String[] sidoNames = {
            "서울", "부산", "대구", "인천",
            "광주", "대전", "울산", "경기",
            "강원", "충북", "충남", "전북",
            "전남", "경북", "경남", "제주", "세종"
        };
        for (String sidoName : sidoNames) {
            this.updateDust(sidoName);
            System.out.println("dust update done with sido name = " + sidoName);
        }
    }

    public void updateDust(String sidoName) throws IOException {
        String airKoreaDustWithQueryStringUrl =
            airKoreaDustUrl +
                String.format("?serviceKey=%s", airKoreaServiceKey) +
                String.format("&numOfRows=%d", numOfRows) +
                String.format("&pageNo=%d", pageNo) +
                String.format("&sidoName=%s", sidoName) +
                String.format("&ver=%f", version) +
                String.format("&_returnType=%s", returnType);

        Map<String, String> headerConfig = new HashMap<>();
        headerConfig.put("Accept", "application/json");

        JsonNode responseBody = DustAlarmCommon.getResponseBodyFromUrl(airKoreaDustWithQueryStringUrl, headerConfig);
        JsonNode jsonDustInfoList = responseBody.get("list");

        for (JsonNode jsonDustInfo : jsonDustInfoList) {
            Map<String, Integer> pm10DustMap = this.getValueAndGradeOfDust(jsonDustInfo, "pm10");
            Map<String, Integer> pm25DustMap = this.getValueAndGradeOfDust(jsonDustInfo, "pm25");

            String stationName = jsonDustInfo.get("stationName").asText();
            String dataTime = jsonDustInfo.get("dataTime").asText();

            Station station = null;
            // Air Korea API에서 station은 없지만 dust는 주는 경우가 있음 :(
            try {
                station = dustAlarmService.findStationByName(stationName);
            } catch (EmptyResultDataAccessException e) {
                continue;
            }

            Integer concentrationId = null;
            try {
                concentrationId = dustAlarmService.findConcentrationByStationId(station.getId()).getId();
            } catch (EmptyResultDataAccessException e) {
            }


            ForecastConcentration forecastConcentration = null;
            try {
                forecastConcentration = dustAlarmService.findFCByRegionName(sidoName);
            } catch (EmptyResultDataAccessException e) {
                forecastConcentration = new ForecastConcentration(
                    sidoName,
                    null,
                    null,
                    null,
                    null);

                dustAlarmService.saveFC(forecastConcentration);
            }

            dustAlarmService.saveConcentration(
                new Concentration(
                    concentrationId,
                    pm10DustMap.get("pm10Value"),
                    pm10DustMap.get("pm10Grade1h"),
                    pm25DustMap.get("pm25Value"),
                    pm25DustMap.get("pm25Grade1h"),
                    dataTime,
                    station,
                    forecastConcentration
                )
            );
        }
    }

    // dustType = "pm10" or "pm25"
    private Map<String, Integer> getValueAndGradeOfDust(JsonNode jsonDustInfo, String dustType) {
        Map<String, Integer> dustValueAndGradeMap = new HashMap<>();
        String dustValueName = dustType + "Value";
        String dustGradeName = dustType + "Grade1h";

        try {
            dustValueAndGradeMap.put(dustValueName, jsonDustInfo.get(dustValueName).asInt());
            dustValueAndGradeMap.put(dustGradeName, jsonDustInfo.get(dustGradeName).asInt());
        } catch (NullPointerException e) {
            dustValueAndGradeMap.put(dustValueName, 0);
            dustValueAndGradeMap.put(dustGradeName, 1);
        }

        return dustValueAndGradeMap;
    }
}
