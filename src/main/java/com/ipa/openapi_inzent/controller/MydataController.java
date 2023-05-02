package com.ipa.openapi_inzent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mydata")
public class MydataController {
    @GetMapping("/providerTable")
    public String providerTable() {
        return "/mydata/mdProviderTable";
    }

    @GetMapping("/collectorTable")
    public String collectorTable() {
        return "/mydata/mdCollectorTable";
    }
    @GetMapping("/agencyTable")
    public String mdServiceTable() {
        return "/mydata/mdAgencyTable";
    }
    @GetMapping("/statistics-Daily")
    public String statistics_Daily() {
        return "/mydata/statistics-Daily";
    }

    @GetMapping("/statistics-7Day")
    public String statistics_7Day() {
        return "/mydata/statistics-7Day";
    }
    @GetMapping("/mydataToken")
    public String mydataToken() {
        return "/mydata/mydataToken";
    }
    @GetMapping("/mydataServiceControl")
    public String mydataServiceControl() {
        return "/mydata/mydataServiceControl";
    }

    @GetMapping("/mydataSendReq")
    public String mydataSendReq() {
        return "/mydata/mydataSendReq";
    }
}
