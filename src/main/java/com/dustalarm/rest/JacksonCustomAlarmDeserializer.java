package com.dustalarm.rest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import com.dustalarm.model.Alarm;
import com.dustalarm.model.User;
import com.dustalarm.service.DustAlarmService;

import java.io.IOException;

public class JacksonCustomAlarmDeserializer extends StdDeserializer<Alarm> {

    public JacksonCustomAlarmDeserializer() {
        this(null);
    }

    public JacksonCustomAlarmDeserializer(Class<User> t) {
        super(t);
    }

    @Autowired
    private DustAlarmService dustAlarmService;

    @Override
    public Alarm deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        JsonNode node = parser.getCodec().readTree(parser);
        Alarm alarm = new Alarm();
        int userId = node.get("user").asInt();
        int alarmConfig = node.get("alarm_config").asInt();
        String time = node.get("time").asText();
        int stationId = node.get("station").asInt();
        boolean activated = node.get("activated").asBoolean();

        String officialAddress;
        if (node.get("official_address") == null) {
            officialAddress = null;
        } else {
            officialAddress = node.get("official_address").asText();
        }

        alarm.setUser(this.dustAlarmService.findUserById(userId));
        alarm.setAlarmConfig(this.dustAlarmService.findAlarmConfigById(alarmConfig));
        alarm.setTime(time);
        alarm.setStation(this.dustAlarmService.findStationById(stationId));
        alarm.setActivated(activated);
        alarm.setOfficialAddress(officialAddress);

        return alarm;
    }
}
