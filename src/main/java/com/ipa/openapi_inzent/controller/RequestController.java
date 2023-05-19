package com.ipa.openapi_inzent.controller;

import com.ipa.openapi_inzent.model.RequestDTO;
import com.ipa.openapi_inzent.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RequestController {
    RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/requestPage")
    public String Authorization(Model model) {
        List<RequestDTO> list = requestService.selectAll();
        List<RequestDTO> ulist = requestService.reqUserList();

        model.addAttribute("list", list);
        model.addAttribute("ulist", ulist);
        return "requestPage";
    }


}
