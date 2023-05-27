package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipa.openapi_inzent.model.MdAgencyDTO;
import com.ipa.openapi_inzent.model.MdProviderDTO;
import com.ipa.openapi_inzent.model.MdServiceDTO;
import com.ipa.openapi_inzent.service.MydataService;
import io.swagger.v3.core.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
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

        List<MdServiceDTO> list = mydataService.mdServiceSelectModal(id);
        System.out.println("id = " + id);
        System.out.println(list);

        JsonArray array = new JsonArray();

        for (int i = 0; i < list.size(); i++) {
            JsonObject object = new JsonObject();
            object.addProperty("id", list.get(i).getId());
            object.addProperty("agencyId", list.get(i).getAgencyId());
            object.addProperty("clientId", list.get(i).getClientId());
            object.addProperty("mdServiceName", list.get(i).getMdServiceName());
            object.addProperty("domainName", list.get(i).getDomainName());

            JsonElement element = JsonParser.parseString(list.get(i).getCallbackUrl()).getAsJsonObject().get("callbackURL");

            object.add("callbackUrl", element);
            array.add(object);

        }
        System.out.println("MydataController.mdServiceTableModal");
        System.out.println("array = " + array);

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

    @GetMapping("/agencyTable/delete")
    @ResponseBody
    public void agencyTableDeleteRow(int id) {
        System.out.println("id = " + id);
//        mydataService.mdAgencyDelete(id);
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
    public String mydataToken(Model model) {
        model.addAttribute("astList", mydataService.mdAstList());
        System.out.println("========================");
        System.out.println(mydataService.mdAstList());
        System.out.println("========================");
        return "/mydata/mydataToken";
    }

    // mdToken 검색
    @GetMapping("/tokenSearch")
    public String mydataSearch(Model model, String keyword) {
        System.out.println("keyword = " + keyword);
        model.addAttribute("astList", mydataService.mdTokenSearch(keyword));

        return "/mydata/mydataToken";
    }

    @GetMapping("/token/modal")
    @ResponseBody
    public JsonObject tokenDetail(int id) {

        System.out.println("MydataController.tokenDetail");

        System.out.println("id = " + id);

        MdAgencyDTO mdOne = mydataService.mdAstOne(id);
        JsonObject object = new JsonObject();

        System.out.println("mdOne = " + mdOne);

        // mdService
        object.addProperty("clientId", mdOne.getMdServiceDTO().getClientId());
        object.addProperty("mdServiceName", mdOne.getMdServiceDTO().getMdServiceName());
        object.addProperty("domainName", mdOne.getMdServiceDTO().getDomainName());

        JsonElement element = JsonParser.parseString(mdOne.getMdServiceDTO().getCallbackUrl()).getAsJsonObject().get("callbackURL");

        object.add("callbackUrl", element);

        // mdAgency
        object.addProperty("agencyId", mdOne.getId());
        object.addProperty("code", mdOne.getCode());
        object.addProperty("division", mdOne.getDivision());
        object.addProperty("agencyName", mdOne.getName());
        object.addProperty("industry", mdOne.getIndustry());

        object.addProperty("address", mdOne.getAddress());
        object.addProperty("domainName", mdOne.getDomainName());
        object.addProperty("publicApiIp", mdOne.getPublicApiIp());
        object.addProperty("authenticationMethod", mdOne.getAuthenticationMethod());

        object.addProperty("TLSNum", mdOne.getTLSNum());
        object.addProperty("agencyIp", mdOne.getAgencyIp());
        object.addProperty("agencyPort", mdOne.getAgencyPort());

        // mdToken
        // date 시간 변환
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String entryDate = sdf.format(mdOne.getMdTokenDTO().getCreateDate());
        String endDate = sdf.format(mdOne.getMdTokenDTO().getEndDate());


        object.addProperty("tokenId",mdOne.getMdTokenDTO().getTokenId());
        object.addProperty("consumerNum",mdOne.getMdTokenDTO().getConsumerNum());
        object.addProperty("createDate", entryDate);
        object.addProperty("endDate", endDate);
        object.addProperty("accessToken",mdOne.getMdTokenDTO().getAccessToken());


        System.out.println("object = " + object);

        return object;
    }

    @GetMapping("/serviceTable")
    public String serviceTable(Model model) {
        System.out.println(mydataService.mdServiceSelectList());
        model.addAttribute("list", mydataService.mdServiceSelectList());
        return "/mydata/mdServiceControl";
    }

    @GetMapping("/service/modal")
    @ResponseBody
    public JsonObject serviceModal(int id) {
        MdServiceDTO mdServiceDTO = mydataService.mdServiceSelectOne(id);

        JsonObject object = new JsonObject();
        object.addProperty("id", mdServiceDTO.getId());
        object.addProperty("clientId", mdServiceDTO.getClientId());
        object.addProperty("mdServiceName", mdServiceDTO.getMdServiceName());
        object.addProperty("domainName", mdServiceDTO.getDomainName());

        JsonElement element = JsonParser.parseString(mdServiceDTO.getCallbackUrl()).getAsJsonObject().get("callbackURL");

        object.add("callbackUrl", element);

        return object;
    }

    @GetMapping("/service/search")
    public String serviceSearch(Model model, String keyword) {
        System.out.println("keyword : " + keyword);
        System.out.println(mydataService.mdServiceSearchKeyword(keyword));
        model.addAttribute("list", mydataService.mdServiceSearchKeyword(keyword));
        return "/mydata/mdServiceControl";
    }

    @GetMapping("/mydataSendReq")
    public String mydataSendReq() {
        return "/mydata/mydataSendReq";
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    //                            (oﾟvﾟ)ノ  Provider Page  (oﾟvﾟ)ノ                           //
    ///////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/providerTable")
    public String mdProviderSelectAll(Model model) {

        model.addAttribute("list", mydataService.mdProviderSelectAll());
        return "/mydata/mdProviderTable";
    }

    @GetMapping("/provider/selectOne")
    @ResponseBody
    public JsonObject mdProviderSelectAll(int id) {
        System.out.println("id = " + id);
//        null 처리
        MdProviderDTO mdProviderDTO = mydataService.mdProviderSelectOne(id);
        System.out.println("mdProviderDTO = " + mdProviderDTO);
        JsonObject object = new JsonObject();
        object.addProperty("id", mdProviderDTO.getId());
        object.addProperty("reqSEQ", mdProviderDTO.getMdReqInfoDTO().getReqSEQ());
        object.addProperty("apiResources", mdProviderDTO.getApiResources()); //거래고유번호
        object.addProperty("reqHeader", mdProviderDTO.getReqHeader());
        object.addProperty("resMsg", mdProviderDTO.getResMsg()); //응답메세지
        object.addProperty("resData", mdProviderDTO.getResData());
        object.addProperty("statusInfo", mdProviderDTO.getStatusInfo()); //상태정보 ex)마이데이터
        object.addProperty("agencyName", mdProviderDTO.getMdReqInfoDTO().getAgencyName());//기관명
        object.addProperty("serviceName", mdProviderDTO.getMdReqInfoDTO().getServiceName()); //서비스명
        object.addProperty("consumerNum", mdProviderDTO.getMdReqInfoDTO().getConsumerNum()); //통합고객번호
        object.addProperty("code", mdProviderDTO.getMdReqInfoDTO().getCode());
        object.addProperty("tokenExpiryDate", mdProviderDTO.getMdReqInfoDTO().getTokenExpiryDate()); //토큰유효기간
        object.addProperty("reqType", mdProviderDTO.getMdReqInfoDTO().getReqType()); //전송요구타입


        return object;
    }

}
