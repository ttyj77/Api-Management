package com.ipa.openapi_inzent.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:static/");
    }

    private final Gson gson = new Gson();

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder().title("OPEN API")
                .description("Open Api Docs_Mydata").build();
    }

    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(swaggerInfo()).select()
                .apis(
                        RequestHandlerSelectors.basePackage("com.ipa.openapi_inzent")
                )
                .paths(PathSelectors.ant("/**"))
                .build()
                .useDefaultResponseMessages(false);
    }

    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }

    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter(GsonBuilder gsonBuilder) {
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        gsonBuilder.registerTypeAdapter(Json.class, SpringfoxJsonToGsonAdapter.builder().build());
        converter.setGson(gsonBuilder.create());
        return converter;
    }

    //    private final Gson gson = new Gson();
//
//    // ......
//
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
//        gsonHttpMessageConverter.setGson(gson);
//        converters.add(gsonHttpMessageConverter);
//    }

//    @Bean
//    public GsonHttpMessageConverter gsonHttpMessageConverter() {
//        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
//        converter.setGson(gson());
//        return converter;
//    }
//
//    private Gson gson() {
//        final GsonBuilder builder = new GsonBuilder();
//        builder.registerTypeAdapter(Json.class, new SpringfoxJsonToGsonAdapter());
//        return builder.create();
//    }

//    @Bean
//    public HttpMessageConverters customConverter() {
//        Collection<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
//        messageConverters.add(gsonHttpMessageConverter);
//        return new HttpMessageConverters(true, messageConverters);
//    }


    // ......


}