package com.dustalarm.rest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.dustalarm.model.Concentration;

import java.io.IOException;

public class JacksonCustomConcentrationSerializer extends StdSerializer<Concentration> {
    public JacksonCustomConcentrationSerializer() {
        this(null);
    }

    public JacksonCustomConcentrationSerializer(Class<Concentration> t) {
        super(t);
    }

    @Override
    public void serialize(Concentration concentration, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        if (concentration.getId() == null) {
            jgen.writeNullField("id");
        } else {
            jgen.writeNumberField("id", concentration.getId());
        }

        jgen.writeNumberField("fine_dust", concentration.getFineDust());
        jgen.writeNumberField("fine_dust_grade", concentration.getFineDustGrade());
        jgen.writeNumberField("ultra_fine_dust", concentration.getUltraFineDust());
        jgen.writeNumberField("ultra_fine_dust_grade", concentration.getUltraFineDustGrade());
        jgen.writeStringField("data_time", concentration.getDataTime());
        jgen.writeNumberField("station", concentration.getStation().getId());
        jgen.writeStringField("region_name", concentration.getFc().getRegionName());
        jgen.writeStringField("announcement_time", concentration.getFc().getAnnouncementTime());
        jgen.writeStringField("target_date", concentration.getFc().getTargetDate());
        jgen.writeNumberField("forecasting_fine_dust_grade", concentration.getFc().getFineDustGrade());
        jgen.writeNumberField("forecasting_ultra_fine_dust_grade", concentration.getFc().getUltraFineDustGrade());

        jgen.writeEndObject();
    }
}
