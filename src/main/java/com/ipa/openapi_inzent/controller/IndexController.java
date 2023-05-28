package com.ipa.openapi_inzent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping("")
    public String index() {
//        @AuthenticationPrincipal UserCustomDetails details   로그인 된 사용자 정보 가져옴
        return "index";
    }

    @GetMapping("/headSideBar")
    public String apisTest() {
        return "/fragment/headSideBar";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "tables";
    }

    @GetMapping("/main")
    @ResponseBody
    public String main() {
        return "main page";
    }

    @GetMapping("/providerTable")
    public String providerTable() {
        return "mdProviderTable";
    }

    @GetMapping("/CollectorTable")
    public String collectorTable() {
        return "mdCollectorTable";
    }

    @GetMapping("/agencyTable")
    public String mdServiceTable() {
        return "mdAgencyTable";
    }


    @GetMapping("/modal")
    public String modal() {
        return "modalTest";
    }

    @GetMapping("/authorization")
    public String authorization() {
        return "authorization";
    }

    @GetMapping("/register")
    public String regis() {
        return "register";
    }

    @GetMapping("/newSideBar")
    public String newSideBar() {
        return "newSideBar";
    }

}
