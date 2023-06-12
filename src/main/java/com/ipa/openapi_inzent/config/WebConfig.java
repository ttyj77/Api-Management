package com.ipa.openapi_inzent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@CrossOrigin(origins = "http://localhost:9000/", maxAge = 3600)
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:9000/oauth2/token")
                .allowedOrigins("http://localhost:9000/")
                .allowedMethods("*")
                .allowCredentials(false)
                .maxAge(3000);
    }
}