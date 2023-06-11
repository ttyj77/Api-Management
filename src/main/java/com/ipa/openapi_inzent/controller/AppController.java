package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipa.openapi_inzent.config.auth.UserCustomDetails;
import com.ipa.openapi_inzent.model.GetDataDTO;
import com.ipa.openapi_inzent.model.MdAgencyDTO;
import com.ipa.openapi_inzent.service.GetDataService;
import com.ipa.openapi_inzent.service.MydataService;
import com.ipa.openapi_inzent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

        if (userDetails == null) {
            System.out.println("앱 에러페이지 드가쟈!!!!!!!!!");
            String errorMessage = "아이디와 비밀번호를 확인해주세요.";

            errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
            return "redirect:/app/login?error=true&exception=" + errorMessage;
        }


        model.addAttribute("user", userDetails.getUserDTO());
        return "/app/main";
    }

    // 인증을 받은 사용자가 로그아웃가능 로그아웃은 SecurityContextLogoutHandler이친구가 진행함
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
            JsonObject jsonObject = (JsonObject) parser.parse(getDataDTO.getResponseData());

            array2.add(jsonObject);

        }

        object.addProperty("bankList", array.toString());
        object.addProperty("agency", array1.toString());
        object.addProperty("balance", array2.toString());


        System.out.println("object = " + object);
        return object;
    }

    @GetMapping("/app/bank/transactions/{account}")
    public String bankDetail(Model model, @AuthenticationPrincipal UserCustomDetails userDetails, @PathVariable String account) {

        System.out.println("account = " + account);
        if (userDetails.getUserDTO() == null) {
            return "redirect:/app/login";
        }
        String clientNum = userDetails.getUserDTO().getOwnNum();

        String uri_1 = "/accounts";
        String uri_2 = "/accounts/deposit/transactions";
        List<GetDataDTO> list = getDataService.selectAll(clientNum, uri_1);
        List<GetDataDTO> list2 = getDataService.selectAll(clientNum, uri_2);

//        int i = 0;
//        for (GetDataDTO getDataDTO : list) {
//            JsonParser parser = new JsonParser();
//            JsonObject jsonObject = (JsonObject) parser.parse(getDataDTO.getResponseData());
//            jsonObject.get("account_list")[i].get("")
//        }


        model.addAttribute("reqAccountInfo", list.get(0).getRequestData());
        model.addAttribute("transInfo", list2.get(0));
        model.addAttribute("accountNum", account);


        System.out.println("ll"+list.get(0).getRequestData());
        System.out.println("list = " + list.get(0));
        System.out.println("list2 = " + list2.get(0));

        return "/app/bankDetail";
    }



    // ### 투자 ###


    // ### 보험 ###


    // ### 통신 ###
}
