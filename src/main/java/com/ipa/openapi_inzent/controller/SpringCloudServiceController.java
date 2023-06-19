package com.ipa.openapi_inzent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringCloudServiceController {

    @GetMapping("/service")
    public String springCloudService() {

        return "spring-cloud-service 호출!";
    }

}

