package com.dustalarm.rest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.dustalarm.model.User;

import java.io.IOException;

public class JacksonCustomUserDeserializer extends StdDeserializer<User> {

    public JacksonCustomUserDeserializer() {
        this(null);
    }

    public JacksonCustomUserDeserializer(Class<User> t) {
        super(t);
    }

    @Override
    public User deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        JsonNode node = parser.getCodec().readTree(parser);
        User user = new User();
        String uuid = node.get("uuid").asText();
        String pushToken = node.get("push_token").asText(null);

        user.setUuid(uuid);
        user.setPushToken(pushToken);
        return user;
    }
}
