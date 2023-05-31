//package com.ipa.openapi_inzent.config;
//
//import com.google.gson.GsonBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.json.GsonHttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.json.Json;
//import springfox.documentation.spring.web.plugins.Docket;
//
//@Configuration
//@EnableWebMvc
//public class SwaggerConfig {
//
//    private ApiInfo swaggerInfo() {
//        return new ApiInfoBuilder().title("OPEN API")
//                .description("Open Api Docs_Mydata").build();
//    }
//
//    @Bean
//    public Docket swaggerApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(swaggerInfo()).select()
//                .apis(
//                        RequestHandlerSelectors.basePackage("com.ipa.openapi_inzent")
//                )
//                .paths(PathSelectors.ant("/**"))
//                .build()
//                .useDefaultResponseMessages(false);
//    }
//
//    @Bean
//    public InternalResourceViewResolver defaultViewResolver() {
//        return new InternalResourceViewResolver();
//    }
//}