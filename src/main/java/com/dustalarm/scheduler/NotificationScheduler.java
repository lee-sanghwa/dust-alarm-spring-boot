package com.dustalarm.scheduler;

import com.dustalarm.security.DustAlarmKeys;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import com.turo.pushy.apns.auth.ApnsSigningKey;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import com.dustalarm.model.Alarm;
import com.dustalarm.model.Concentration;
import com.dustalarm.service.DustAlarmService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class NotificationScheduler {

    @Autowired
    private DustAlarmService dustAlarmService;
    private String APNS_KEY_ID = DustAlarmKeys.APNS_KEY_ID;
    private String APNS_AUTH_KEY = NotificationScheduler.class.getResource(".").getPath() + DustAlarmKeys.apnsAuthKeyName;
    private String TEAM_ID = DustAlarmKeys.TEAM_ID;
    private String BUNDLE_ID = DustAlarmKeys.BUNDLE_ID;
    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void dustAlarmNotificate() throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        Date date = new Date();
        String currentTime = dateFormat.format(date);
        List<Alarm> alarms = new ArrayList<>(this.dustAlarmService.findAlarmByTimeActivated(currentTime));

        for (Alarm alarm : alarms) {
            String REGISTRATION_ID = alarm.getUser().getPushToken();
            String officialAddress = alarm.getOfficialAddress();
            Concentration alarmConcentration = dustAlarmService.findConcentrationByStationId(alarm.getStation().getId());
            Integer fineDust = alarmConcentration.getFineDust();
            Integer ultraFineDust = alarmConcentration.getUltraFineDust();
            String dataTime = alarmConcentration.getDataTime();

            String body = String.format(
                "미세먼지: %s\n" +
                    "초미세먼지: %s\n" +
                    "%s",
                fineDust2String(fineDust),
                ultraFineDust2String(ultraFineDust),
                dataTimeWithFormat(dataTime));

            ApnsClient apnsClient = new ApnsClientBuilder()
                .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
                .setSigningKey(ApnsSigningKey.loadFromPkcs8File(new File(APNS_AUTH_KEY), TEAM_ID, APNS_KEY_ID))
                .build();

            String payload = new ApnsPayloadBuilder()
                .setAlertTitle(officialAddress)
                .setAlertBody(body)
                .setSound("default")
                .buildWithDefaultMaximumLength();

            final String token = TokenUtil.sanitizeTokenString(REGISTRATION_ID);

            SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, BUNDLE_ID, payload);

            apnsClient.sendNotification(pushNotification);
        }
    }

    private String fineDust2String(Integer fineDust) {
        if (fineDust.intValue() <= 30) {
            return String.format("좋음 %s㎍/㎥ (0 ~ 30)", fineDust.intValue());
        } else if (fineDust.intValue() <= 50) {
            return String.format("보통 %s㎍/㎥ (31 ~ 50)", fineDust.intValue());
        } else if (fineDust.intValue() <= 100) {
            return String.format("나쁨 %s㎍/㎥ (51 ~ 100)", fineDust.intValue());
        } else {
            return String.format("매우나쁨 %s㎍/㎥ (101 ~ )", fineDust.intValue());
        }
    }

    private String ultraFineDust2String(Integer ultraFineDust) {
        if (ultraFineDust.intValue() <= 15) {
            return String.format("좋음 %s㎍/㎥ (0 ~ 15)", ultraFineDust.intValue());
        } else if (ultraFineDust.intValue() <= 25) {
            return String.format("보통 %s㎍/㎥ (16 ~ 25)", ultraFineDust.intValue());
        } else if (ultraFineDust.intValue() <= 50) {
            return String.format("나쁨 %s㎍/㎥ (26 ~ 50)", ultraFineDust.intValue());
        } else {
            return String.format("매우나쁨 %s㎍/㎥ (51 ~ )", ultraFineDust.intValue());
        }
    }

    private String dataTimeWithFormat(String dataTime) {
        String[] dataTimeList = dataTime.split(" ");
        String date = dataTimeList[0];
        String time = dataTimeList[1];

        String[] timeList = time.split(":");
        String hour = timeList[0];
        int hourWithInt = Integer.parseInt(hour);
        String minute = timeList[1];

        if (hourWithInt < 12) {
            return String.format("%s / %s AM 기준", date, time);
        } else if (hourWithInt == 12) {
            return String.format("%s / %s PM 기준", date, time);
        } else if (hourWithInt < 24) {
            int pm_hour = hourWithInt - 12;
            time = String.format("%02d", pm_hour) + String.format(":%s", minute);
            return String.format("%s / %s PM 기준", date, time);
        } else {
            int pm_hour = hourWithInt - 12;
            time = String.format("%d", pm_hour) + String.format(":%s", minute);
            return String.format("%s / %s AM 기준", date, time);
        }
    }
}
