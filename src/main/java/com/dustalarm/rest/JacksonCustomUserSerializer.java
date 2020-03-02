package com.dustalarm.rest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.dustalarm.model.User;

import java.io.IOException;

public class JacksonCustomUserSerializer extends StdSerializer<User> {

    public JacksonCustomUserSerializer() {
        this(null);
    }

    public JacksonCustomUserSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(User user, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        if (user.getId() == null) {
            jgen.writeNullField("id");
        } else {
            jgen.writeNumberField("id", user.getId());
        }
        jgen.writeStringField("uuid", user.getUuid());
        jgen.writeStringField("push_token", user.getPushToken());
        if (user.getCreatedTime() == null) {
            jgen.writeNullField("createdTime");
        } else {
            jgen.writeStringField("createdTime", user.getCreatedTime().toString());
        }
        if (user.getUpdatedTime() == null) {
            jgen.writeNullField("createdTime");
        } else {
            jgen.writeStringField("updatedTime", user.getUpdatedTime().toString());
        }
        jgen.writeEndObject();
    }
}
