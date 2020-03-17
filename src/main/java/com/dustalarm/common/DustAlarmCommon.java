package com.dustalarm.common;

import com.dustalarm.model.Alarm;
import com.dustalarm.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

public class DustAlarmCommon {

    public static Alarm setUserAgent(Alarm target, String userAgent) {
        target.setVersion(userAgent);
        return target;
    }

    public static Collection<User> setUserAgent2Users(Collection<User> users, String userAgent) {
        for (User user : users) {
            user.setVersion(userAgent);
        }
        return users;
    }

    public static Collection<Alarm> setUserAgent2Alarms(Collection<Alarm> targets, String userAgent) {
        for (Alarm target : targets) {
            target.setVersion(userAgent);
        }
        return targets;
    }

    public static JsonNode getResponseBodyFromUrl(String url, Map<String, String> headerConfig) throws IOException {
        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        /* Using Lambda For setting connection header */
        headerConfig.forEach(connection::setRequestProperty);
        connection.connect();

        /* get Json Format From Body */
        BufferedReader tempBr = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String tempLine;
        while ((tempLine = tempBr.readLine()) != null) {
            sb.append(tempLine + "\n");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseBody = mapper.readTree(sb.toString());
        tempBr.close();
        connection.disconnect();

        return responseBody;
    }
}
