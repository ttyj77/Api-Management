package com.ipa.openapi_inzent.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Configuration
@Configuration
public class Swagger2Config {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("v1-definition")
                .pathsToMatch("/invest/**")
                .build();
    }

    @Bean
    public Docket api() {

        //Adding Header
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder.name("Authorization")                 // name of header
                .modelRef(new ModelRef("string"))
                .parameterType("header")               // type - header
                .required(true)                // for compulsory
                .build();
        List<Parameter> aParameters = new ArrayList<>();
        aParameters.add(aParameterBuilder.build());             // add parameter
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors
                        .any())
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/invest/**")
                .globalOperationParameters(aParameters)
                .apiInfo(metaData())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()));


    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Infinity API")
                        .description("INFINITY 프로젝트 API 명세서입니다.")
                        .version("v0.0.1"));
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("CherishPet REST API")
                .description("CherishPet rest api documentation")
                .license("Apache License Version 2.0")
                .build();
    }

}
