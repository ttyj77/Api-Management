package com.ipa.openapi_inzent.config;

import com.google.gson.*;
import springfox.documentation.spring.web.json.Json;

import java.lang.reflect.Type;

public class SwaggerJsonSerializer implements JsonSerializer<Json> {
    @Override
    public JsonElement serialize(Json json, Type type, JsonSerializationContext context) {
        final JsonParser parser = new JsonParser();
        return parser.parse(json.value());
    }
}
