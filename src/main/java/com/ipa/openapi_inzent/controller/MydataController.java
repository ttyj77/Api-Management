package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipa.openapi_inzent.model.*;
import com.ipa.openapi_inzent.service.GetDataService;
import com.ipa.openapi_inzent.service.MydataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/mydata")
public class MydataController {

    private MydataService mydataService;
    GetDataService getDataService;

    @Autowired
    public MydataController(MydataService mydataService, GetDataService getDataService) {
        this.mydataService = mydataService;
        this.getDataService = getDataService;
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
        System.out.println(" " + id);
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


    /////////////////////////////////////////////////
    // TOKEN start
    /////////////////////////////////////////////////
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


        object.addProperty("tokenId", mdOne.getMdTokenDTO().getTokenId());
        object.addProperty("consumerNum", mdOne.getMdTokenDTO().getConsumerNum());
        object.addProperty("createDate", entryDate);
        object.addProperty("endDate", endDate);
        object.addProperty("accessToken", mdOne.getMdTokenDTO().getAccessToken());


        System.out.println("object = " + object);

        return object;
    }

    /////////////////////////////////////////////////
    // TOKEN end
    /////////////////////////////////////////////////

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
        model.addAttribute("list", mydataService.mdServiceSearchKeyword(keyword));
        return "/mydata/mdServiceControl";
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    //                            (oﾟvﾟ)ノ  Provider Page  (oﾟvﾟ)ノ                           //
    ///////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/providerTable")
    public String mdProviderSelectAll(Model model) {
        List<MdProviderDTO> list = mydataService.mdProviderSelectAll();
        model.addAttribute("list", list);
        return "/mydata/mdProviderTable";
    }

    @GetMapping("/provider/insert")
    public void mdProviderInsert(MdProviderDTO mdProviderDTO) {
        // 특정 코드 실행 되기 전 시간
        long start = System.currentTimeMillis();
        System.out.println("start = " + start);

        // 전송 하는 부분(코드 작성 필요)
        int sum = 0;
//        for (int i = 0; i < 10000000; i++) {
//            sum += i;
//        }

        mydataService.mdProviderSelectAll();
        // 특정 코드 실행 되고 난 후 시간
        long end = System.currentTimeMillis();
        System.out.println("end = " + end);

        // 초 단위 실행시간
        double result = (end - start) / 1000.0;
        System.out.println("result = " + result);

        // to_json으로 db들어가기전에 타입 변환해줘야함
//        mdProviderDTO.setRuntime(result);


//        mydataService.mdProviderInsert(mdProviderDTO);


    }

    @GetMapping("/provider/selectOne")
    @ResponseBody
    public JsonObject mdProviderSelectOne(int id) {
        System.out.println("id = " + id);
//        null 처리
        MdProviderDTO mdProviderDTO = mydataService.mdProviderSelectOne(id);
        System.out.println("mdProviderDTO = " + mdProviderDTO);
        JsonObject object = new JsonObject();
        object.addProperty("id", mdProviderDTO.getId());
        object.addProperty("reqSEQ", mdProviderDTO.getMdReqInfoDTO().getReqSEQ());
        System.out.println("mdProviderDTO.getMdReqInfoDTO().getReqSEQ() = " + mdProviderDTO.getMdReqInfoDTO().getReqSEQ());
        // api 리소스 명에 contracts 있으면 provider 상세 정보에 피보험자 순번 column 추가
        String apiResources = mdProviderDTO.getApiResources();
        boolean contain = false;
        if (apiResources.indexOf("contracts") != -1) {
            // 해당 문자 포함
            System.out.println("api resource에 contracts 포함");
            contain = true;
        }
        System.out.println("contain = " + contain);

        object.addProperty("apiResources", apiResources);
        object.addProperty("reqHeader", mdProviderDTO.getReqHeader());
        object.addProperty("resMsg", mdProviderDTO.getResMsg()); //응답메세지
        object.addProperty("resData", mdProviderDTO.getResData());
        object.addProperty("statusInfo", mdProviderDTO.getStatusInfo()); //상태정보 ex)마이데이터
        object.addProperty("agencyName", mdProviderDTO.getMdReqInfoDTO().getAgencyName());//기관명
        object.addProperty("serviceName", mdProviderDTO.getMdReqInfoDTO().getServiceName()); //서비스명
        object.addProperty("consumerNum", mdProviderDTO.getCustomerNum()); //통합고객번호
        object.addProperty("code", mdProviderDTO.getMdReqInfoDTO().getCode());//거래고유번호
        object.addProperty("reqType", mdProviderDTO.getMdReqInfoDTO().getReqType()); //전송요구타입
        object.addProperty("uniqueNum", mdProviderDTO.getUniqueNum());

        if (mdProviderDTO.getMdReqInfoDTO().getTokenExpiryDate() == null) {
            object.addProperty("tokenExpiryDate", ""); //토큰유효기간
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tokenExpiryDate = sdf.format(mdProviderDTO.getMdReqInfoDTO().getTokenExpiryDate());
            object.addProperty("tokenExpiryDate", tokenExpiryDate); //토큰유효기간
        }

        return object;
    }

    @GetMapping("/providerSearch")
    public String mdProviderSearch(Model model, String keyword) {
        System.out.println("keyword = " + keyword);
        model.addAttribute("list", mydataService.mdProviderSearch(keyword));

        return "/mydata/mdProviderTable";
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    //                            (oﾟvﾟ)ノ  Collector Page  (oﾟvﾟ)ノ                           //
    ///////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/collectorTable")
    public String collectorTable(Model model) {

        List<MdCollectorDTO> list = mydataService.mdCollectorSelectAll();

        System.out.println("list = " + list);

        model.addAttribute("list", list);
        return "/mydata/mdCollectorTable";
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    //                            (oﾟvﾟ)ノ  Send Request Page  (oﾟvﾟ)ノ                       //
    ///////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/mydataSendReq")
    public String mydataSendReq(Model model) {
        List<MdReqInfoDTO> list = mydataService.mdReqAll();
        System.out.println("MydataController.mydataSendReq");
        model.addAttribute("list", list);

        System.out.println("list = " + list);

        System.out.println("list.size() = " + list.size());
        return "/mydata/mydataSendReq";
    }
    @GetMapping("/selectToken")
    @ResponseBody
    public JsonObject selectToken(String orgCode) {
        System.out.println("MydataController.selectToken");
        System.out.println("code = " + orgCode);
        MdAgencyDTO mdAgencyDTO = mydataService.mdAstOrgCode(orgCode);
        System.out.println("mdAgencyDTO = " + mdAgencyDTO);
        JsonObject object = new JsonObject();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd Hh:mm");
        String issueDate = date.format(mdAgencyDTO.getMdTokenDTO().getCreateDate());
        String expireDate = date.format(mdAgencyDTO.getMdTokenDTO().getEndDate());

        object.addProperty("token", mdAgencyDTO.getMdTokenDTO().getAccessToken());

        object.addProperty("clientId",mdAgencyDTO.getMdTokenDTO().getConsumerNum() );
        object.addProperty("serviceName",mdAgencyDTO.getMdServiceDTO().getMdServiceName());
        object.addProperty("orgCode", mdAgencyDTO.getCode());
        object.addProperty("agencyName",mdAgencyDTO.getName());

        object.addProperty("issueDate", issueDate);
        object.addProperty("expireDate", expireDate);
        return object;
    }

    @PostMapping("/mdTakeAccount")
    @ResponseBody
    public JsonObject mdTakeAccount(String clientNum, String orgCode) {
        System.out.println("MydataController.mdTakeAccount======================");
        // 계좌번호 넣을 JSON
        JsonObject object = new JsonObject();

        System.out.println("clientNum = " + clientNum);
        System.out.println("orgCode = " + orgCode);
        String uri_1 = "/accounts";

        // 일단 fix시킴 정보주체번호
        GetDataDTO getDataDTO = getDataService.getAccount("943578", uri_1, orgCode);

        System.out.println("getDataDTO = " + getDataDTO);

        JsonArray array = new JsonArray();

        JsonObject jsonObject = (JsonObject) JsonParser.parseString(getDataDTO.getResponseData());
        System.out.println("jsonObject = " + jsonObject);
        System.out.println("jsonObject.get(\"account_list\") = " + jsonObject.get("account_list"));
        array = jsonObject.get("account_list").getAsJsonArray();
        System.out.println("array = " + array);

        System.out.println("array.size() = " + array.size());

        JsonArray accountList = new JsonArray();
        for (int i = 0; i < array.size(); i++) {
            JsonObject accJson = (JsonObject) array.get(i);
            accountList.add(getString(String.valueOf(accJson.get("account_num"))));
        }

        System.out.println("accountList = " + accountList);

        object.addProperty("accountList", accountList.toString());

        System.out.println("object = " + object);


        return object;
    }

    @GetMapping("/provider/customerList")
    @ResponseBody
    public JsonObject customerList(String customerNum, String code) {

        JsonObject object = new JsonObject();
        List<MdProviderDTO> list = mydataService.mdProviderCustomerList(customerNum, code);
        System.out.println("customerNum = " + customerNum);
        System.out.println("MydataController.customerList-=-=-=-=-");
//        System.out.println("list = " + list);

//        object.addProperty("");

        JsonArray providerArray = new JsonArray();
        for (MdProviderDTO mdProviderDTO : list) {
            JsonObject r = new JsonObject();
            r.addProperty("id", mdProviderDTO.getId());
            r.addProperty("reqDate", mdProviderDTO.getReqDate());
            r.addProperty("reqTime", mdProviderDTO.getReqTime());
            r.addProperty("resDate", mdProviderDTO.getResDate());
            r.addProperty("runtime", mdProviderDTO.getRuntime());
            r.addProperty("code", mdProviderDTO.getResCode());
            r.addProperty("apiCode", mdProviderDTO.getApiCode());
            r.addProperty("customerNum", customerNum);
            r.addProperty("regularTransmission", mdProviderDTO.getRegularTransmission());

            providerArray.add(r);
        }

        object.addProperty("providerList", providerArray.toString());
        System.out.println("object = " + object);

        return object;
    }

    @GetMapping("/reqSearch")
    public String mdReqSearch(Model model, String keyword) {
        System.out.println("keyword = " + keyword);
        model.addAttribute("list", mydataService.mdReqSearch(keyword));

        return "/mydata/mydataSendReq";
    }

    ////////////
    //  달력   //
    ////////////

    @GetMapping("/calendarSend")
    @ResponseBody
    public JsonObject calendarInSendReq(String dday, String customerNum, String org_code) {
        JsonObject object = new JsonObject();
        List<MdProviderDTO> list = mydataService.mdReqList();
        System.out.println("dday = " + dday);
        System.out.println("MydataController.calendar=-=-=-=-");
        System.out.println("o = " + org_code);

        JsonArray providerArray = new JsonArray();
        for (MdProviderDTO mdProviderDTO : list) {
            System.out.println("mdProviderDTO.getReqDate() = " + mdProviderDTO.getReqDate());
            String reqDate = mdProviderDTO.getReqDate();
            System.out.println("reqDate.equals(dday) = " + reqDate.equals(dday));
            System.out.println(dday.equals(reqDate) && customerNum.equals(mdProviderDTO.getCustomerNum()));
            System.out.println("mdProviderDTO = " + mdProviderDTO);
            System.out.println("mdProviderDTO = " + mdProviderDTO.getMdReqInfoDTO().getCode());
            if (dday.equals("") || dday.equals(reqDate)) {
                if (customerNum.equals(mdProviderDTO.getCustomerNum())&& mdProviderDTO.getMdReqInfoDTO().getCode().equals(org_code)) {
                    System.out.println("들어옴");

                    JsonObject r = new JsonObject();

                    r.addProperty("id", mdProviderDTO.getId());

                    r.addProperty("reqDate", reqDate);
                    r.addProperty("reqTime", mdProviderDTO.getReqTime());
                    r.addProperty("resDate", mdProviderDTO.getResDate());
                    r.addProperty("runtime", mdProviderDTO.getRuntime());
                    r.addProperty("code", mdProviderDTO.getResCode());
                    r.addProperty("apiCode", mdProviderDTO.getApiCode());
                    r.addProperty("customerNum", mdProviderDTO.getCustomerNum());
//                    r.addProperty("regularTransmission", mdProviderDTO.getRegularTransmission());

                    providerArray.add(r);
                }
            }
        }

        object.addProperty("providerList", providerArray.toString());
        System.out.println("object = " + object);

        return object;
    }

    @GetMapping("/provider/calendar")
    @ResponseBody
    public JsonObject calendarInSendReq(String dday) {
        JsonObject jsonObject = new JsonObject();
        System.out.println("dday = " + dday);
        System.out.println("MydataController.calendarInSendReq");

        List<MdProviderDTO> list = mydataService.mdProviderSelectAll();

        System.out.println(list);

        JsonArray r = new JsonArray();
        for (MdProviderDTO mdProviderDTO : list) {
            String reqDate = mdProviderDTO.getReqDate();
            if (dday.equals("") || dday.equals(reqDate)) {

                JsonObject object = new JsonObject();
                object.addProperty("id", mdProviderDTO.getId());
                object.addProperty("reqDate", reqDate);
                object.addProperty("reqTime", mdProviderDTO.getReqTime());
                object.addProperty("resDate", mdProviderDTO.getResDate());
                object.addProperty("runtime", mdProviderDTO.getRuntime());
                object.addProperty("resCode", mdProviderDTO.getResCode());
                object.addProperty("apiCode", mdProviderDTO.getApiCode());
                object.addProperty("customerNum", mdProviderDTO.getCustomerNum());
//                object.addProperty("regularTransmission", mdProviderDTO.getRegularTransmission());
                object.addProperty("uniqueNum", mdProviderDTO.getUniqueNum());
                object.addProperty("statusInfo", mdProviderDTO.getStatusInfo());
//                object.addProperty("apiResources", mdProviderDTO.getApiResources());
//                object.addProperty("resMsg", mdProviderDTO.getResMsg());
//                object.addProperty("reqHeader", mdProviderDTO.getReqHeader());
//                object.addProperty("resData", mdProviderDTO.getResData());

                r.add(object);
            }

        }

        jsonObject.addProperty("providerList", r.toString());
        System.out.println("jsonObject = " + jsonObject);
        return jsonObject;
    }



    @GetMapping("/statistics/{orgCode}/{date}")
    public String statistics(Model model, @PathVariable String orgCode, @PathVariable String date) throws ParseException {
        System.out.println("MydataController.showChart");
        System.out.println("orgCode = " + orgCode);
        System.out.println("date = " + date);

        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
        Date day = dayFormat.parse(date);
        model.addAttribute("day", day);

        // 총 호출 횟수 + 성공 + 실패 횟수들
        DailyApiStatisticsDTO dailyApiOne = getDataService.dailyAPIStatisticsOne(date, orgCode);
        System.out.println("dailyApiOne = " + dailyApiOne);

        // 리소스들 사용 빈도
        List<DailyApiSeqDTO> dailyApiSeq = getDataService.dailyApiSeq(orgCode, date);
        System.out.println("dailyApiSeq = " + dailyApiSeq);

        // 에러코드 빈도 수
        List<DailyApiErrorDTO> dailyApiError = getDataService.dailyApiError(orgCode, date);
        System.out.println("dailyApiError = " + dailyApiError);

        // 에러코드 내용 출력
        List<ErrorDTO> errorList = getDataService.errorAll();
        System.out.println("errorList = " + errorList);

        // 에러코드 빈도 수 넣을 최종 리스트
        List<ErrorDTO> errorSeqList = new ArrayList<>();

        for (DailyApiErrorDTO d : dailyApiError) {
            for (ErrorDTO e : errorList) {
                if (d.getResCode().equals(e.getError())) {
                    ErrorDTO temp = new ErrorDTO();
                    temp.setError(d.getResCode());
                    temp.setSeq(d.getCount());
                    temp.setReason(e.getReason());
                    errorSeqList.add(temp);
                }
            }
        }
        System.out.println("errorSeqList = " + errorSeqList);

        model.addAttribute("dailyApiOne", dailyApiOne);
        model.addAttribute("dailyApiSeq", dailyApiSeq);
        model.addAttribute("errorSeqList", errorSeqList);

        return "/mydata/statistics";
    }

    @GetMapping("/chartData")
    @ResponseBody
    public JsonObject chartData(String date, String code) {
        System.out.println("MydataController.chartData======================");
        System.out.println("date = " + date);
        System.out.println("code = " + code);
        JsonObject object = new JsonObject();

        // 총 호출 횟수 + 성공 + 실패 횟수들
        List<DailyApiStatisticsDTO> dailyTimeList = getDataService.dailyTimeCall(code, date);
        System.out.println("dailyTimeList = " + dailyTimeList);

        JsonArray timeArr = new JsonArray();
        for (DailyApiStatisticsDTO d : dailyTimeList) {
            JsonObject tArr = new JsonObject();
            tArr.addProperty("date", d.getDate());
            tArr.addProperty("code", d.getCode());
            tArr.addProperty("hh", d.getHh());
            tArr.addProperty("successCnt", d.getSuccessCnt());
            tArr.addProperty("failCnt", d.getFailCnt());
            timeArr.add(tArr);
        }
        object.addProperty("timeList", timeArr.toString());

        // 리소스들 사용 빈도
        List<DailyApiSeqDTO> dailyApiSeq = getDataService.dailyApiSeq(code, date);
        System.out.println("dailyApiSeq = " + dailyApiSeq);
        JsonArray resourcesArr = new JsonArray();
        for (DailyApiSeqDTO d : dailyApiSeq) {
            JsonObject rArr = new JsonObject();
            rArr.addProperty("apiResource", d.getApiResources());
            rArr.addProperty("seq", d.getSeq());
            resourcesArr.add(rArr);
        }
        object.addProperty("resourcesSeq", resourcesArr.toString());

        // 에러코드 빈도 수
        List<DailyApiErrorDTO> dailyApiError = getDataService.dailyApiError(code, date);
        System.out.println("dailyApiError = " + dailyApiError);

        // 에러코드 내용 출력
        List<ErrorDTO> errorList = getDataService.errorAll();
        System.out.println("errorList = " + errorList);

        // 에러코드 빈도 수 넣을 최종 리스트
        List<ErrorDTO> errorSeqList = new ArrayList<>();

        for (DailyApiErrorDTO d : dailyApiError) {
            for (ErrorDTO e : errorList) {
                if (d.getResCode().equals(e.getError())) {
                    ErrorDTO temp = new ErrorDTO();
                    temp.setError(d.getResCode());
                    temp.setSeq(d.getCount());
                    temp.setReason(e.getReason());
                    errorSeqList.add(temp);
                }
            }
        }
        JsonArray errorArr = new JsonArray();
        for (ErrorDTO e : errorSeqList) {
            JsonObject err = new JsonObject();
            err.addProperty("error", e.getError());
            err.addProperty("reason", e.getReason());
            err.addProperty("seq", e.getSeq());
            errorArr.add(err);
        }
        object.addProperty("errorSeq", errorArr.toString());

        return object;
    }

    @GetMapping("/statistics/calendar")
    @ResponseBody
    public JsonObject statisticsCalendar(String dday) throws ParseException {

        List<DailyApiStatisticsDTO> dailyApiList = getDataService.dailyAPIStatistics();
        System.out.println("d = " + dday);

        System.out.println("MydataController.statisticsCalendar");
        JsonObject object = new JsonObject();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
        System.out.println("dailyApiList = " + dailyApiList);


        JsonArray array = new JsonArray();
        for (DailyApiStatisticsDTO d : dailyApiList) {
            if (dday.equals("") || dday.equals(d.getDate())) {
                JsonObject dObj = new JsonObject();
//            Date date = dayFormat.parse(String.valueOf(d.getDate()));
//            System.out.println("date = " + date);
//            String day = sdf.format(date);

                dObj.addProperty("date", d.getDate());
                dObj.addProperty("name", d.getName());
                dObj.addProperty("code", d.getCode());
                dObj.addProperty("logo", d.getLogo());
                dObj.addProperty("industry", d.getIndustry());
                dObj.addProperty("totalRequest", d.getTotalRequest());
                dObj.addProperty("successCnt", d.getSuccessCnt());
                dObj.addProperty("failCnt", d.getFailCnt());

                array.add(dObj);
            }
        }
        object.addProperty("dailyApiList", array.toString());

        return object;

    }

    @GetMapping("/statistics/search")
    @ResponseBody
    public JsonObject statisticsSearch(String keyword) {
        List<DailyApiStatisticsDTO> dailyApiList = getDataService.dailyStatisticsSearch(keyword);
        System.out.println("keyword = " + keyword);

        System.out.println("MydataController.statisticsSearch");
        JsonObject object = new JsonObject();

        System.out.println("dailyApiList = " + dailyApiList);

        JsonArray array = new JsonArray();
        for (DailyApiStatisticsDTO d : dailyApiList) {
            JsonObject dObj = new JsonObject();

            dObj.addProperty("date", d.getDate());
            dObj.addProperty("name", d.getName());
            dObj.addProperty("code", d.getCode());
            dObj.addProperty("logo", d.getLogo());
            dObj.addProperty("industry", d.getIndustry());
            dObj.addProperty("totalRequest", d.getTotalRequest());
            dObj.addProperty("successCnt", d.getSuccessCnt());
            dObj.addProperty("failCnt", d.getFailCnt());

            array.add(dObj);
        }
        object.addProperty("dailyApiList", array.toString());

        return object;

    }

    @GetMapping("/dailyStatistics")
    public String showDaily(Model model) {

        System.out.println("MydataController.showDaily");
        List<DailyApiStatisticsDTO> dailyApiList = getDataService.dailyAPIStatistics();

        System.out.println("dailyApiList = " + dailyApiList);

        model.addAttribute("dailyApiList", dailyApiList);

        return "/mydata/dailyStatistics";
    }

    // "" 없애주는 function
    private static String getString(String str) {
        String newStr = str.replaceAll("\\\"", "");
        return newStr;
    }
}
