package com.dustalarm.scheduler;

import com.dustalarm.common.DustAlarmCommon;
import com.dustalarm.security.DustAlarmKeys;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import com.dustalarm.model.ForecastConcentration;
import com.dustalarm.service.DustAlarmService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ForecastDustScheduler {

    @Autowired
    private DustAlarmService dustAlarmService;
    private String airKoreaFCUrl = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMinuDustFrcstDspth";
    private String airKoreaServiceKey = DustAlarmKeys.airKoreaServiceKey;
    private String returnType = "json";
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Scheduled(cron = "0 10,12,14,16,18,20 5,11,17,23 * * *")
    public void updateFC() throws IOException, NullPointerException {
        Date date = new Date();
        String currentDate = dateFormat.format(date);

        String airKoreaFCWithQueryStringUrl =
            airKoreaFCUrl +
                String.format("?serviceKey=%s", airKoreaServiceKey) +
                String.format("&searchDate=%s", currentDate) +
                String.format("&_returnType=%s", returnType);

        Map<String, String> headerConfig = new HashMap<>();
        headerConfig.put("Accept", "application/json");

        JsonNode responseBody = DustAlarmCommon.getResponseBodyFromUrl(airKoreaFCWithQueryStringUrl, headerConfig);
        JsonNode jsonFCInfoList = responseBody.get("list");

        Map<String, String> todayPM10Map = this.getTodayFC(jsonFCInfoList, "PM10", currentDate);
        Map<String, String> todayPM25Map = this.getTodayFC(jsonFCInfoList, "PM25", currentDate);

        Map<String, Integer> todayRegionPM10Map = this.getRegionFC(todayPM10Map.get("informGrade"));
        Map<String, Integer> todayRegionPM25Map = this.getRegionFC(todayPM25Map.get("informGrade"));

        List<String> regionNames = new ArrayList<String>(todayRegionPM10Map.keySet());

        for (String regionName : regionNames) {
            dustAlarmService.saveFC(
                new ForecastConcentration(
                    regionName,
                    todayPM10Map.get("dataTime"),
                    todayPM10Map.get("informData"),
                    todayRegionPM10Map.get(regionName),
                    todayRegionPM25Map.get(regionName)
                )
            );
        }

    }


    private Map<String, String> getTodayFC(JsonNode jsonFCInfoList, String dustType, String currentDate) {
        Map<String, String> todayFCMap = new HashMap<>();

        for (JsonNode jsonFCInfo : jsonFCInfoList) {
            String informCode = jsonFCInfo.get("informCode").asText();
            String informData = jsonFCInfo.get("informData").asText();
            if (dustType.equals(informCode) && currentDate.equals(informData)) {
                String dataTime = jsonFCInfo.get("dataTime").asText();
                String informGrade = jsonFCInfo.get("informGrade").asText();

                todayFCMap.put("dataTime", dataTime);
                todayFCMap.put("informCode", informCode);
                todayFCMap.put("informData", informData);
                todayFCMap.put("informGrade", informGrade);
                return todayFCMap;
            }
        }
        // 에러 상황
        return null;
    }

    private Map<String, Integer> getRegionFC(String informGrades) {
        Map<String, Integer> regionFC = new HashMap<>();

        String[] informGradesArray = informGrades.split(",");
        for (String informGrade : informGradesArray) {
            String[] regionGrade = informGrade.split(" : ");
            String regionName = regionGrade[0];
            switch (regionName) {
                case "경기북부":
                case "경기남부":
                    regionName = "경기";
                    break;
                case "영동":
                case "영서":
                    regionName = "강원";
                    break;
            }

            switch (regionGrade[1]) {
                case "좋음":
                    regionFC.put(regionName, 1);
                    break;
                case "보통":
                    regionFC.put(regionName, 2);
                    break;
                case "나쁨":
                    regionFC.put(regionName, 3);
                    break;
                case "매우나쁨":
                    regionFC.put(regionName, 4);
                    break;
            }
        }
        return regionFC;
    }

    public static void main(String[] args) throws IOException {
        new ForecastDustScheduler().updateFC();
    }
}
