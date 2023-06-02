package com.ipa.openapi_inzent.config;//package com.ipa.openapi_inzent.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import springfox.documentation.spring.web.json.Json;
//
@Configuration
public class GsonHttpMessageConverterConfig {

    @Bean
    public Gson gson() {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Json.class, new SpringfoxJsonToGsonAdapter());
        return builder.create();
    }
}
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import org.springframework.http.converter.json.GsonHttpMessageConverter;
//import springfox.documentation.spring.web.json.Json;
//
//public class GsonHttpMessageConverterConfig extends GsonHttpMessageConverter {
//
//    public GsonHttpMessageConverter buildGson() {
//        final Gson gson = new GsonBuilder()
//                .registerTypeAdapter(Json.class, new SwaggerJsonSerializer())
//                .serializeNulls()
//                .create();
//
//        final GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
//        converter.setGson(gson);
//        return converter;
//    }
//
//}