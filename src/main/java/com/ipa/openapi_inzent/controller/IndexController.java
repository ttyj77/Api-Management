package com.ipa.openapi_inzent.controller;

import com.ipa.openapi_inzent.model.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    @GetMapping("")
    public String index() {
//        @AuthenticationPrincipal UserCustomDetails details   로그인 된 사용자 정보 가져옴
        return "/newIndex";
    }


    @GetMapping("/headSideBar")
    public String apisTest() {
        return "/fragment/headSideBar";
    }

    @GetMapping("/test")
    @ResponseBody
    public UserDTO test(HttpSession session) {
        UserDTO logIn = (UserDTO) session.getAttribute("logIn");
        return logIn;
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



}
