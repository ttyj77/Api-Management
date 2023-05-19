package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonObject;
import com.ipa.openapi_inzent.model.RequestDTO;
import com.ipa.openapi_inzent.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

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
        System.out.println("list = " + list);


        model.addAttribute("list", list);
        return "requestPage";
    }

    @GetMapping("/selectOne/{id}")
    @ResponseBody
    public JsonObject selectOne(@PathVariable int id){
        JsonObject object = new JsonObject();
        RequestDTO requestDTO = requestService.selectOne(id);

        object.addProperty("id", id);
        object.addProperty("entryDate", String.valueOf(requestDTO.getEntryDate()));
        object.addProperty("procDate", String.valueOf(requestDTO.getProcDate()));
        object.addProperty("procUsername", requestDTO.getProcUsername());
        object.addProperty("title", requestDTO.getTitle());
        object.addProperty("content", requestDTO.getContent());
        object.addProperty("status", requestDTO.getStatus());
        object.addProperty("userId", requestDTO.getUserId());
        object.addProperty("reqUsername", requestDTO.getReqUsername());
        object.addProperty("reqNickname", requestDTO.getReqNickname());


        return object;
    }


}
