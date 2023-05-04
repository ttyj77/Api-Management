package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipa.openapi_inzent.model.MdAgencyDTO;
import com.ipa.openapi_inzent.model.MdServiceDTO;
import com.ipa.openapi_inzent.service.MydataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/mydata")
public class MydataController {

    private MydataService mydataService;

    @Autowired
    public MydataController(MydataService mydataService) {
        this.mydataService = mydataService;
    }


    @GetMapping("/providerTable")
    public String providerTable() {
        return "/mydata/mdProviderTable";
    }

    @GetMapping("/collectorTable")
    public String collectorTable() {
        return "/mydata/mdCollectorTable";
    }

    @GetMapping("/agencyTable")
    public String mdServiceTable(Model model) {

        model.addAttribute("agencyList", mydataService.mdAgencySelectAll());
        return "/mydata/mdAgencyTable";
    }

    /* 마이데이터 기관 목록의 기관정보 모달 */
    @GetMapping("/agencyTable/modal")
    @ResponseBody
    public JsonObject mdServiceTableModal(int id) {

        List<MdServiceDTO> list = mydataService.mdServiceSelectAll(id);


        JsonArray array = new JsonArray();

        for (int i = 0; i < list.size(); i++) {
            JsonObject object = new JsonObject();
            object.addProperty("id", list.get(i).getId());
            object.addProperty("agencyId", list.get(i).getAgencyId());
            object.addProperty("clientId", list.get(i).getClientId());
            object.addProperty("mdServiceName", list.get(i).getMdServiceName());
            object.addProperty("domainName", list.get(i).getDomainName());

            JsonElement element = JsonParser.parseString(list.get(i).getCallbackUrl()).getAsJsonObject().get("url");

            object.add("callbackUrl", element);
            array.add(object);

        }

        JsonObject result = new JsonObject();

        result.addProperty("status", "200");
        result.addProperty("message", "success");
        result.addProperty("responseText", array.toString());


        return result;
    }

    @GetMapping("/agencyTable/selectOne")
    @ResponseBody
    public JsonObject selectAgencyOne(int id) {

        MdAgencyDTO mdAgencyDTO = mydataService.mdAgencySelectOne(id);

        JsonObject object = new JsonObject();
        object.addProperty("id", mdAgencyDTO.getId());
        object.addProperty("code", mdAgencyDTO.getCode());
        object.addProperty("division", mdAgencyDTO.getDivision());
        object.addProperty("name", mdAgencyDTO.getName());
        object.addProperty("industry", mdAgencyDTO.getIndustry());

        object.addProperty("address", mdAgencyDTO.getAddress());
        object.addProperty("domainName", mdAgencyDTO.getDomainName());
        object.addProperty("publicApiIp", mdAgencyDTO.getPublicApiIp());
        object.addProperty("authenticationMethod", mdAgencyDTO.getAuthenticationMethod());

        object.addProperty("TLSNum", mdAgencyDTO.getTLSNum());
        object.addProperty("agencyIp", mdAgencyDTO.getAgencyIp());
        object.addProperty("agencyPort", mdAgencyDTO.getAgencyPort());

        JsonObject result = new JsonObject();

        result.addProperty("status", "200");
        result.addProperty("message", "success");
        result.addProperty("responseText", object.toString());


        return result;
    }

    @GetMapping("/agencyTable/selectBox")
    @ResponseBody
    public JsonObject agencySelectBox(String division) {

        List<MdAgencyDTO> list = new ArrayList<>();
        if (division.equals("전체")) {
            list = mydataService.mdAgencySelectAll();
        } else {
            list = mydataService.mdAgencySelectBox(division);
        }


        JsonArray array = new JsonArray();
        for (int i = 0; i < list.size(); i++) {
            JsonObject object = new JsonObject();
            object.addProperty("id", list.get(i).getId());
            object.addProperty("code", list.get(i).getCode());
            object.addProperty("division", list.get(i).getDivision());
            object.addProperty("name", list.get(i).getName());

            object.addProperty("industry", list.get(i).getIndustry());

            object.addProperty("address", list.get(i).getAddress());
            object.addProperty("domainName", list.get(i).getDomainName());
            object.addProperty("publicApiIp", list.get(i).getPublicApiIp());
            object.addProperty("authenticationMethod", list.get(i).getAuthenticationMethod());

            object.addProperty("TLSNum", list.get(i).getTLSNum());
            object.addProperty("agencyIp", list.get(i).getAgencyIp());
            object.addProperty("agencyPort", list.get(i).getAgencyPort());


            array.add(object);

        }

        JsonObject result = new JsonObject();

        result.addProperty("status", "200");
        result.addProperty("message", "success");
        result.addProperty("responseText", array.toString());


        return result;
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
