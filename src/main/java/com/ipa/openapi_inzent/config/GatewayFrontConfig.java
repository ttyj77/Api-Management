package com.ipa.openapi_inzent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;

@Configuration
public class GatewayFrontConfig {
    @Bean
    RouterFunction staticResourceLocator() {
        return RouterFunctions.resources("/spring-cloud-service/**", new ClassPathResource("static/css/"));
    }
}
