package com.dustalarm.rest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.dustalarm.model.Alarm;

import java.io.IOException;

public class JacksonCustomAlarmSerializer extends StdSerializer<Alarm> {
    public JacksonCustomAlarmSerializer() {
        this(null);
    }

    public JacksonCustomAlarmSerializer(Class<Alarm> t) {
        super(t);
    }

    @Override
    public void serialize(Alarm alarm, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        if (alarm.getId() == null) {
            jgen.writeNullField("id");
        } else {
            jgen.writeNumberField("id", alarm.getId());
        }

        jgen.writeNumberField("user", alarm.getUser().getId());
        jgen.writeNumberField("alarm_config", alarm.getAlarmConfig().getId());
        jgen.writeStringField("time", alarm.getTime());
        if ("2.0.0".equals(alarm.getVersion())) {
            jgen.writeObjectField("station", alarm.getStation());
        } else {
            jgen.writeNumberField("station", alarm.getStation().getId());

        }
        jgen.writeBooleanField("activated", alarm.getActivated());
        jgen.writeStringField("official_address", alarm.getOfficialAddress());

        jgen.writeEndObject();
    }
}
