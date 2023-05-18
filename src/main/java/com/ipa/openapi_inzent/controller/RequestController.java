package com.ipa.openapi_inzent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RequestController {


    @Autowired
    public RequestController() {

    }

    @GetMapping("/requestPage")
    public String Authorization() {
        return "requestPage";
    }


}
