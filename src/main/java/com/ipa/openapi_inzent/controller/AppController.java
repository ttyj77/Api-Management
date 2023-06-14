package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipa.openapi_inzent.config.auth.UserCustomDetails;
import com.ipa.openapi_inzent.model.GetDataDTO;
import com.ipa.openapi_inzent.model.MdAgencyDTO;
import com.ipa.openapi_inzent.model.TransactionDataDTO;
import com.ipa.openapi_inzent.model.UserDTO;
import com.ipa.openapi_inzent.service.GetDataService;
import com.ipa.openapi_inzent.service.MydataService;
import com.ipa.openapi_inzent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class AppController {

    UserService userService;
    GetDataService getDataService;
    MydataService mydataService;

    @Autowired
    public AppController(UserService userService, GetDataService getDataService, MydataService mydataService) {
        this.getDataService = getDataService;
        this.userService = userService;
        this.mydataService = mydataService;
    }

    @GetMapping("/app/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "applogin";
    }


    @GetMapping("/app/main")
    public String main(Model model, @AuthenticationPrincipal UserCustomDetails userDetails) throws UnsupportedEncodingException {
        System.out.println("userDetails = " + userDetails);

        UserDTO logIn = userDetails.getUserDTO();

        String uri_2 = "/accounts/deposit/detail";
        List<GetDataDTO> list = getDataService.selectAll(logIn.getOwnNum(), uri_2);


        if (userDetails == null) {
            System.out.println("앱 에러페이지 드가쟈!!!!!!!!!");
            String errorMessage = "아이디와 비밀번호를 확인해주세요.";

            errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
            return "redirect:/app/login?error=true&exception=" + errorMessage;
        }

        System.out.println("list = " + list);

        int sum = 0;

        for (int i = 0; i < list.size(); i++) {
            JsonObject jsonObject = (JsonObject) JsonParser.parseString(list.get(i).getResponseData());
            JsonArray results = jsonObject.get("detail_list").getAsJsonArray();
            JsonObject tempJson = (JsonObject) results.get(0);
            String balance = getString(String.valueOf(tempJson.get("balance_amt")));
            System.out.println("balance = " + balance);
            sum += Integer.parseInt(balance);

        }

        model.addAttribute("myProperty", sum);
        model.addAttribute("user", userDetails.getUserDTO());
        return "/app/main";
    }

    // "" 없애주는 function
    private static String getString(String str) {
        String newStr = str.replaceAll("\\\"", "");
        return newStr;
    }

    // 인증을 받은 사용자가 로그아웃가능 로그아웃은 SecurityContextLogoutHandler 이친구가 진행함
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // authentication 이 널이 아니면 로그아웃진행
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/";
    }

    @GetMapping("/app/agencyChoice")
    public String agencyChoice() {

        return "/app/agencyChoice";
    }

    // ### 카드 ###
    @GetMapping("/app/cardDetail")
    public String cardDetail() {
        //파라미터들 있어야함 ( 카드 정보 )
        return "/app/cardDetail";
    }

    // 추가
    @GetMapping("/app/card/insert")
    public String cardInsert() {
        return "/app/cardInsert";
    }


    // ### 은행 ###
    @GetMapping("/app/bank/insert")
    public String bank() {

        return "/app/bankInsert";
    }

    @PostMapping("/app/bank/myAccount")
    @ResponseBody
    public JsonObject myAccount(String clientNum) {

        // 계좌 정보 조회 api resources
        String uri_1 = "/accounts";
        String uri_2 = "/accounts/deposit/detail";
        List<GetDataDTO> list1 = getDataService.selectAll(clientNum, uri_1);
        List<GetDataDTO> list2 = getDataService.selectAll(clientNum, uri_2);
        List<MdAgencyDTO> agencyDTOList = mydataService.mdAgencySelectAll();
        JsonObject object = new JsonObject();

        JsonArray array = new JsonArray();
        System.out.println("list = " + list1);
        System.out.println("list2 = " + list2);
        for (GetDataDTO g : list1) {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject) parser.parse(g.getResponseData());
            JsonObject jsonObject1 = (JsonObject) parser.parse(g.getRequestData());

            array.add(jsonObject1);
            array.add(jsonObject);
        }

        JsonArray array1 = new JsonArray();

        for (MdAgencyDTO m : agencyDTOList) {
            JsonObject object1 = new JsonObject();
            object1.addProperty("org_code", m.getCode());
            object1.addProperty("bankName", m.getName());
            object1.addProperty("logo", m.getLogo());

            array1.add(object1);
        }

        // 잔액 가져오기 위해
        JsonArray array2 = new JsonArray();

        for (GetDataDTO getDataDTO : list2) {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject1 = (JsonObject) parser.parse(getDataDTO.getRequestData());
            JsonObject jsonObject = (JsonObject) parser.parse(getDataDTO.getResponseData());

            array2.add(jsonObject1);
            array2.add(jsonObject);
        }

        object.addProperty("bankList", array.toString());
        object.addProperty("agency", array1.toString());
        object.addProperty("balance", array2.toString());

        System.out.println("object = " + object);
        return object;
    }

    @GetMapping("/app/bank/transactions/{account}")
    public String bankDetail(Model model, @AuthenticationPrincipal UserCustomDetails userDetails, @PathVariable String account) throws ParseException {

        System.out.println("account = " + account);
        if (userDetails.getUserDTO() == null) {
            return "redirect:/app/login";
        }

        String clientNum = userDetails.getUserDTO().getOwnNum();

        String uri_2 = "/accounts/deposit/detail";
        String uri_3 = "/accounts/deposit/transactions";
        List<GetDataDTO> balanceList = getDataService.accountAll(account, clientNum, uri_2);
        List<GetDataDTO> accountList = getDataService.accountAll(account, clientNum, uri_3);
        List<MdAgencyDTO> agencyDTOList = mydataService.mdAgencySelectAll();

        System.out.println("agencyDTOList = " + agencyDTOList);

        System.out.println("accountList = " + accountList);

        System.out.println("balanceList = " + balanceList);

        // 은행 명, logo 출력 용
        JsonObject object = (JsonObject) JsonParser.parseString(accountList.get(0).getRequestData());
        String org_code = String.valueOf(object.get("org_code"));
        String org = getString(org_code);
        System.out.println("org = " + org);

        for (MdAgencyDTO agencyDTO : agencyDTOList) {
            if (agencyDTO.getCode().equals(org)) {
                model.addAttribute("agencyDTO", agencyDTO);
                System.out.println("agencyDTO = " + agencyDTO);
            }
        }

        // 잔액 용
        JsonObject object1 = (JsonObject) JsonParser.parseString(balanceList.get(0).getResponseData());
        System.out.println("================================");
        System.out.println(object1.get("detail_list"));
        JsonArray results = object1.get("detail_list").getAsJsonArray();
        System.out.println("results = " + results);
        JsonObject object2 = (JsonObject) results.get(0);
        System.out.println("object2 = " + object2);
        String balance_amt = String.valueOf(object2.get("balance_amt"));
        System.out.println("balance = " + balance_amt);
        String balance = getString(balance_amt);
        System.out.println("balance = " + balance);

        model.addAttribute("balance", balance);

        // 거래 내역
        // 거래내역 response Data 넣을 DTO List
        List<TransactionDataDTO> transList = new ArrayList<>();

        JsonObject jsonObject = (JsonObject) JsonParser.parseString(accountList.get(0).getResponseData());
        System.out.println("jsonObject = " + jsonObject);

        JsonArray results1 = jsonObject.get("trans_list").getAsJsonArray();
        System.out.println("results1.size = " + results1.size());
        System.out.println("results1 = " + results1);

        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        for (int i = results1.size() - 1; i >= 0; i--) {
            TransactionDataDTO temp = new TransactionDataDTO();

            JsonObject tempJson = (JsonObject) results1.get(i);
            temp.setTrans_amt(getString(String.valueOf(tempJson.get("trans_amt"))));
            temp.setTrans_type(getString(String.valueOf(tempJson.get("trans_type"))));
            temp.setBalance_amt(getString(String.valueOf(tempJson.get("balance_amt"))));
            temp.setTrans_class(getString(String.valueOf(tempJson.get("trans_class"))));
            Date date = dayFormat.parse(getString(String.valueOf(tempJson.get("trans_dtime"))));
            temp.setTrans_dtime(date);
            temp.setCurrency_code(getString(String.valueOf(tempJson.get("currency_code"))));

            transList.add(temp);
        }

        System.out.println("transList = " + transList);
        model.addAttribute("accountNum", account);
        model.addAttribute("transList", transList);
        System.out.println("model = " + model);

        return "/app/bankDetail";
    }


    // ### 투자 ###


    // ### 보험 ###


    // ### 통신 ###


    // ### 인증 페이지 ###
    // ( 가입상품 목록 전송 요구서 )
    @GetMapping("/app/certificationSendReq/{list}")
    public String certificationSendReq(Model model, @PathVariable List<String> list) {
        System.out.println("list = " + list);
        model.addAttribute("list", list);
        return "/app/certificationSendReq";
    }

    @GetMapping("/app/infoAgreement")
    public String certificationIndividualInfoAgree() {

        return "/app/certificationIndividualInfoAgree";
    }

    @GetMapping("/app/certificationChoice")
    public String certificationChoice() {
        return "/app/certificationChoice";
    }

    @GetMapping("/app/addProperty")
    public String addProperty() {
        return "/app/addProperty";
    }
}
