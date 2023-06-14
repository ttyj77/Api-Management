package com.ipa.openapi_inzent.config.swagger;//package com.ipa.openapi_inzent.config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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