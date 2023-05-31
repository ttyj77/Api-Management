package com.ipa.openapi_inzent.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import lombok.experimental.SuperBuilder;
import springfox.documentation.spring.web.json.Json;

@SuperBuilder
public class SpringfoxJsonToGsonAdapter implements JsonSerializer<Json> {
//    @Override
//    public JsonElement serialize(String json, Type typeOfSrc, JsonSerializationContext context) {
//        if (json.contains("openapi")) {
//            return JsonParser.parseString(json.replace("\n", ""));
//        } else {
//            return new Gson().toJsonTree(json, typeOfSrc);
//        }
//    }

    @Override
    public JsonElement serialize(Json json, Type type, JsonSerializationContext context) {
        final JsonParser parser = new JsonParser();
        return parser.parse(json.value());
    }


}
