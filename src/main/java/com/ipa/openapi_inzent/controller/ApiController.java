package com.ipa.openapi_inzent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class ApiController {
    @GetMapping("")
    public String apis() {
        return "/apis/index";
    }

    @GetMapping("/details")
    public String details() {
        return "/apis/details";
    }

    @GetMapping("/trash")
    public String apiTrash() {
        return "/apis/trash";
    }

    @GetMapping("/resourceModal")
    public String resourceModal() {
        return "/apis/resourceModal";
    }
}
