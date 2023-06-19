package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipa.openapi_inzent.config.auth.UserCustomDetails;
import com.ipa.openapi_inzent.model.*;
import com.ipa.openapi_inzent.service.GetDataService;
import com.ipa.openapi_inzent.service.MydataApiService;
import com.ipa.openapi_inzent.service.MydataService;
import com.ipa.openapi_inzent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AppController {

    UserService userService;
    GetDataService getDataService;
    MydataService mydataService;
    MydataApiService mydataApiService;

    @Autowired
    public AppController(UserService userService, GetDataService getDataService, MydataService mydataService, MydataApiService mydataApiService) {
        this.getDataService = getDataService;
        this.userService = userService;
        this.mydataService = mydataService;
        this.mydataApiService = mydataApiService;
    }

    @GetMapping("/app/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "applogin";
    }


    @GetMapping("/app/main")
    public String main(Model model, @AuthenticationPrincipal UserCustomDetails userDetails, HttpSession session) throws UnsupportedEncodingException {
        List<String> list1 = (List<String>) session.getAttribute("choiceAgency");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = new UserDTO();
        if (principal instanceof UserDetails) {
            //일반로그인
            String username = ((UserDetails) principal).getUsername();
            userDTO = userDetails.getUserDTO();
        } else {
            //인젠트 로그인
            UserDTO logIn = (UserDTO) session.getAttribute("logIn");
            String username = principal.toString();
            userDTO = logIn;
        }

//        UserDTO logIn = userDTO;

        String uri_2 = "/accounts/deposit/detail";
        List<GetDataDTO> list = getDataService.selectAll(userDTO.getOwnNum(), uri_2);


        if (userDTO == null) {
            System.out.println("앱 에러페이지 드가쟈!!!!!!!!!");
            String errorMessage = "아이디와 비밀번호를 확인해주세요.";

            errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
            return "redirect:/app/login?error=true&exception=" + errorMessage;
        }

        session.removeAttribute("choiceAgency");
        System.out.println("userDetails = " + userDetails);

        String uri_3 = "/accounts/transactions";

        List<GetDataDTO> investList = getDataService.selectAll(userDTO.getOwnNum(), uri_3);
        System.out.println("investList = " + investList);

        int sum = 0;

        System.out.println("list = " + list);
        // 고객이 가지고 있는 모든 계좌 조회해서 잔액 더해서 순자산 값 구하기

        for (int i = 0; i < list.size(); i++) {
            JsonObject jsonObject = (JsonObject) JsonParser.parseString(list.get(i).getResponseData());
            JsonArray results = jsonObject.get("detail_list").getAsJsonArray();
            JsonObject tempJson = (JsonObject) results.get(0);
            String balance = getString(String.valueOf(tempJson.get("balance_amt")));
            System.out.println("balance = " + balance);
            sum += Integer.parseInt(balance);

        }

        System.out.println("sum = " + sum);


        System.out.println("investList = " + investList.size());
        for (int i = 0; i < investList.size(); i++) {

            JsonObject jsonObject = (JsonObject) JsonParser.parseString(investList.get(i).getResponseData());
            JsonArray results = jsonObject.get("trans_list").getAsJsonArray();
            if (!results.isEmpty()) {
                System.out.println("results.size() = " + results.size());
                // trans_list 마지막 거래 내역의 잔액을 총 자산에 더해주기
                JsonObject tempJson = (JsonObject) results.get(results.size() - 1); // 마지막 리스트 잔액
                System.out.println("tempJson = " + tempJson);
                String balance = getString(String.valueOf(tempJson.get("balance_amt")));
                System.out.println("balance = " + balance);
                sum += Integer.parseInt(balance);
            }
        }


        System.out.println("sum = " + sum);

        // card 업권 목록 내용


        String uri_1 = "/cards";
        List<GetDataDTO> cardList = getDataService.selectAllIndustry(userDTO.getOwnNum(), uri_1, "card");
        System.out.println("AppController.myCardAccount");
        System.out.println("list1 = " + cardList);
        System.out.println("list1.isEmpty() = " + cardList.isEmpty());

        JsonArray array = new JsonArray();
        for (GetDataDTO g : cardList) {
            JsonParser parser = new JsonParser();
            JsonObject jsonObjectRes = (JsonObject) parser.parse(g.getResponseData());

            array.add(jsonObjectRes);
        }

        System.out.println("array = " + array);
        System.out.println("array.isEmpty() = " + array.isEmpty());

        model.addAttribute("cardInfo", array);
        model.addAttribute("myProperty", sum);
        model.addAttribute("user", userDTO);
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

    @PostMapping("/app/card/myAccount")
    @ResponseBody
    public JsonObject myCardAccount(String clientNum, String industry) {
        JsonObject object = new JsonObject();
        String uri_1 = "/cards";
        List<GetDataDTO> list1 = getDataService.selectAllIndustry(clientNum, uri_1, industry);
        System.out.println("list1 = " + list1);
        System.out.println("list1.isEmpty() = " + list1.isEmpty());

        JsonArray array = new JsonArray();
        for (GetDataDTO g : list1) {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject) parser.parse(g.getResponseData());
            JsonObject jsonObject1 = (JsonObject) parser.parse(g.getRequestData());

            array.add(jsonObject1);
            array.add(jsonObject);
        }

        object.addProperty("cardList", array.toString());

        return object;
    }

    // 추가
    @GetMapping("/app/card/insert")
    public String cardInsert() {
        return "/app/cardInsert";
    }


    // ### 은행 ###
    @GetMapping("/app/bank/insert/{industry}")
    public String bank(Model model, @PathVariable String industry, @AuthenticationPrincipal UserCustomDetails userDetails, HttpSession session) {
        List<MdAgencyDTO> agencyDTOList = mydataService.agencyIndustry(industry);
        System.out.println("agencyDTOList = " + agencyDTOList);
        System.out.println("agencyDTOList.size() = " + agencyDTOList.size());
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = new UserDTO();

        if (principal instanceof UserDetails) {
            //일반로그인
            String username = ((UserDetails) principal).getUsername();
            System.out.println("username 1 = " + username);
            System.out.println((UserDetails) principal);
            userDTO = userDetails.getUserDTO();
        } else {
            //인젠트 로그인
            UserDTO logIn = (UserDTO) session.getAttribute("logIn");
            System.out.println("==============" + logIn);
            String username = principal.toString();
            System.out.println("username 2  = " + username);
            System.out.println("userinfo 2  " + principal);
            userDTO = logIn;
        }


        String uri_1 = "/accounts";

        List<GetDataDTO> org_codeList = getDataService.selectAllIndustry(userDTO.getOwnNum(), uri_1, industry);
        System.out.println("org_codeList = " + org_codeList);

        // 자산 연결된 기관코드 저장할 빈 list
        List<String> connectAgency = new ArrayList<>();
        for (int i = 0; i < org_codeList.size(); i++) {
            JsonObject object = (JsonObject) JsonParser.parseString(org_codeList.get(i).getRequestData());
            String org_code = String.valueOf(object.get("org_code"));
            String org = getString(org_code);
            System.out.println("org = " + org);
            connectAgency.add(org);
        }

        System.out.println("agencyDTOList1111 = " + agencyDTOList.size());
        for (int i = 0; i < agencyDTOList.size(); i++) {
            for (String agency : connectAgency) {
                if (agency.equals(agencyDTOList.get(i).getCode())) {
                    agencyDTOList.remove(i);
                }
            }
        }
        System.out.println("agencyDTOList 22222= " + agencyDTOList.size());

        model.addAttribute("connectAgency", connectAgency);
        model.addAttribute("agencyList", agencyDTOList);
        return "/app/bankInsert";
    }

    @PostMapping("/app/bank/myAccount")
    @ResponseBody
    public JsonObject myBankAccount(String clientNum, String industry) {

        System.out.println("industry = " + industry);
        System.out.println("clientNum = " + clientNum);
        // 계좌 정보 조회 api resources
        String uri_1 = "/accounts";
        String uri_2 = "/accounts/deposit/detail";
        List<GetDataDTO> list1 = getDataService.selectAllIndustry(clientNum, uri_1, industry);
        List<GetDataDTO> list2 = getDataService.selectAllIndustry(clientNum, uri_2, industry);
        List<MdAgencyDTO> agencyDTOList = mydataService.mdAgencySelectAll();
        JsonObject object = new JsonObject();

        JsonArray array = new JsonArray();
        System.out.println("list1 = " + list1);
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
        System.out.println("array2.toString() = " + array2.toString());

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

    @GetMapping("/app/invest/insert/{industry}")
    public String invest(Model model, @PathVariable String industry, @AuthenticationPrincipal UserCustomDetails userDetails, HttpSession session) {
        System.out.println("AppController.invest!!!!!!!!!!!!!!!!!!!");
        List<MdAgencyDTO> agencyDTOList = mydataService.agencyIndustry(industry);
        System.out.println("agencyDTOList = " + agencyDTOList);
        System.out.println("agencyDTOList.size() = " + agencyDTOList.size());

//        UserDTO logIn = userDetails.getUserDTO();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = new UserDTO();

        if (principal instanceof UserDetails) {
            //일반로그인
            String username = ((UserDetails) principal).getUsername();
            System.out.println("username 1 = " + username);
            System.out.println((UserDetails) principal);
            userDTO = userDetails.getUserDTO();
        } else {
            //인젠트 로그인
            UserDTO logIn = (UserDTO) session.getAttribute("logIn");
            System.out.println("==============" + logIn);
            String username = principal.toString();
            System.out.println("username 2  = " + username);
            System.out.println("userinfo 2  " + principal);
            userDTO = logIn;
        }


        String uri_1 = "/accounts";

        List<GetDataDTO> org_codeList = getDataService.selectAllIndustry(userDTO.getOwnNum(), uri_1, industry);
        System.out.println("org_codeList = " + org_codeList);

        // 자산 연결된 기관코드 저장할 빈 list
        List<String> connectAgency = new ArrayList<>();
        for (int i = 0; i < org_codeList.size(); i++) {
            JsonObject object = (JsonObject) JsonParser.parseString(org_codeList.get(i).getRequestData());
            String org_code = String.valueOf(object.get("org_code"));
            String org = getString(org_code);
            System.out.println("org = " + org);
            connectAgency.add(org);
        }

        System.out.println("agencyDTOList1111 = " + agencyDTOList.size());
        for (int i = 0; i < agencyDTOList.size(); i++) {
            for (String agency : connectAgency) {
                if (agency.equals(agencyDTOList.get(i).getCode())) {
                    agencyDTOList.remove(i);
                }
            }
        }
        System.out.println("agencyDTOList 22222= " + agencyDTOList.size());

        model.addAttribute("connectAgency", connectAgency);

        ///////

        model.addAttribute("agencyList", agencyDTOList);
        return "/app/investInsert";
    }

    @PostMapping("/app/invest/myAccount")
    @ResponseBody
    public JsonObject myInvestAccount(String clientNum, String industry) {

        System.out.println("industry = " + industry);
        System.out.println("clientNum = " + clientNum);
        // 계좌 정보 조회 api resources
        String uri_1 = "/accounts";
        String uri_2 = "/accounts/basic";
        String uri_3 = "/accounts/transactions";

        List<GetDataDTO> list1 = getDataService.selectAllIndustry(clientNum, uri_1, industry);
        List<GetDataDTO> list2 = getDataService.selectAllIndustry(clientNum, uri_2, industry);
        List<MdAgencyDTO> agencyDTOList = mydataService.mdAgencySelectAll();
        List<GetDataDTO> investList = getDataService.selectAll(clientNum, uri_3);

        System.out.println("investList = " + investList);

        JsonObject object = new JsonObject();

        JsonArray array = new JsonArray();
        System.out.println("list1 = " + list1);
        System.out.println("list2 = " + list2);

        // 계좌 정보
        for (GetDataDTO g : list1) {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject1 = (JsonObject) parser.parse(g.getRequestData());
            JsonObject jsonObject = (JsonObject) parser.parse(g.getResponseData());

            array.add(jsonObject1);
            array.add(jsonObject);
        }

        // 기관 리스트 가져오기
        JsonArray array1 = new JsonArray();

        for (MdAgencyDTO m : agencyDTOList) {
            JsonObject object1 = new JsonObject();
            object1.addProperty("org_code", m.getCode());
            object1.addProperty("agencyName", m.getName());
            object1.addProperty("logo", m.getLogo());

            array1.add(object1);
        }


        // 잔액 가져오기 위해
        List<String> balance = new ArrayList<>();
        for (int i = 0; i < investList.size(); i++) {

            JsonObject jsonObject = (JsonObject) JsonParser.parseString(investList.get(i).getResponseData());
            JsonArray results = jsonObject.get("trans_list").getAsJsonArray();
            if (results.isEmpty()) {
                balance.add("0");
            } else {
                System.out.println("results.size() = " + results.size());
                // trans_list 마지막 거래 내역의 잔액을 총 자산에 더해주기
                JsonObject tempJson = (JsonObject) results.get(results.size() - 1); // 마지막 리스트 잔액
                balance.add(getString(String.valueOf(tempJson.get("balance_amt"))));
            }
            System.out.println("balance = " + balance);
        }

        JsonArray balanceArray = new JsonArray();


        for (GetDataDTO getDataDTO : investList) {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject1 = (JsonObject) parser.parse(getDataDTO.getRequestData());
            JsonObject jsonObject = (JsonObject) parser.parse(getDataDTO.getResponseData());

            balanceArray.add(jsonObject1);
            balanceArray.add(jsonObject);
        }


        // 계좌 디테일 정보 가져오는 부분
        JsonArray array2 = new JsonArray();

        for (GetDataDTO getDataDTO : list2) {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject1 = (JsonObject) parser.parse(getDataDTO.getRequestData());
            JsonObject jsonObject = (JsonObject) parser.parse(getDataDTO.getResponseData());

            array2.add(jsonObject1);
            array2.add(jsonObject);
        }

        object.addProperty("investList", array.toString());
        object.addProperty("agency", array1.toString());
        object.addProperty("balance", balanceArray.toString());

        System.out.println("object = " + object);
        return object;
    }

    @GetMapping("/app/invest/transactions/{account}")
    public String investDetail(Model model, @AuthenticationPrincipal UserCustomDetails userDetails, @PathVariable String account) throws ParseException {

        System.out.println("account = " + account);
        if (userDetails.getUserDTO() == null) {
            return "redirect:/app/login";
        }

        String clientNum = userDetails.getUserDTO().getOwnNum();
        String uri_1 = "/accounts";
        String uri_2 = "/accounts/basic";
        String uri_3 = "/accounts/transactions";


        List<GetDataDTO> accountInfo = getDataService.accountOne(account, clientNum, uri_1);
        List<GetDataDTO> mortgageList = getDataService.accountAll(account, clientNum, uri_2);
        List<GetDataDTO> accountList = getDataService.accountAll(account, clientNum, uri_3);
        List<MdAgencyDTO> agencyDTOList = mydataService.mdAgencySelectAll();

        System.out.println("agencyDTOList = " + agencyDTOList);

        System.out.println("accountList = " + accountList);

        System.out.println("mortgageList = " + mortgageList);

        // 계좌 상세 정보 조회 용
        JsonObject accObject = (JsonObject) JsonParser.parseString(accountInfo.get(0).getResponseData());
        JsonArray accArray = accObject.get("account_list").getAsJsonArray();
        System.out.println("accArray = " + accArray);
        System.out.println("accArray.size() = " + accArray.size());


        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
        AccountInfoDTO temp = new AccountInfoDTO();
        JsonObject tempJson = (JsonObject) accArray.get(0);

        temp.setCnt(String.valueOf(accArray.size()));
        temp.setAccount_num(getString(String.valueOf(tempJson.get("account_num"))));
        temp.setAccount_name(getString(String.valueOf(tempJson.get("account_name"))));
        temp.setAccount_type(getString(String.valueOf(tempJson.get("account_type"))));
        Date date = dayFormat.parse(getString(String.valueOf(tempJson.get("issue_date"))));
        temp.setIssue_date(date);

        model.addAttribute("accountInfo", temp);

        // 신용 대출액 / 대출금 용
        JsonObject mortgageObject = (JsonObject) JsonParser.parseString(mortgageList.get(0).getResponseData());
        System.out.println("mortgageObject = " + mortgageObject);
        JsonArray morArray = mortgageObject.get("basic_list").getAsJsonArray();
        System.out.println("morArray = " + morArray);

        JsonObject basicObj = (JsonObject) morArray.get(0);
        System.out.println("basicObj = " + basicObj);

        System.out.println("getString(String.valueOf(basicObj.get(\"mortgage_cmt\"))) = " + getString(String.valueOf(basicObj.get("mortgage_cmt"))));
        System.out.println("getString(String.valueOf(basicObj.get(\"credit_loan_am\"))) = " + getString(String.valueOf(basicObj.get("credit_loan_am"))));

        model.addAttribute("mortgage_cmt", getString(String.valueOf(basicObj.get("mortgage_amt"))));
        model.addAttribute("credit_loan_am", getString(String.valueOf(basicObj.get("credit_loan_amt"))));


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
        JsonObject balanceObject = (JsonObject) JsonParser.parseString(accountList.get(0).getResponseData());
        JsonArray balanceArray = balanceObject.get("trans_list").getAsJsonArray();

        System.out.println("results.size() = " + balanceArray.size());
        // trans_list 마지막 거래 내역의 잔액을 총 자산에 더해주기
        if (balanceArray.size() == 0) {
            model.addAttribute("balance", "0");
        } else {
            JsonObject balanceJson = (JsonObject) balanceArray.get(balanceArray.size() - 1); // 마지막 리스트 잔액
            String balance = getString(String.valueOf(balanceJson.get("balance_amt")));
            System.out.println("balance = " + balance);

            model.addAttribute("balance", balance);
        }


        // 거래 내역
        // 거래내역 response Data 넣을 DTO List
        List<InvestTransactionDTO> transactionList = new ArrayList<>();

        JsonObject transObject = (JsonObject) JsonParser.parseString(accountList.get(0).getResponseData());
        System.out.println("jsonObject = " + transObject);

        JsonArray trans_list = transObject.get("trans_list").getAsJsonArray();
        System.out.println("results1.size = " + trans_list.size());
        System.out.println("results1 = " + trans_list);

        SimpleDateFormat dtimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        for (int i = trans_list.size() - 1; i >= 0; i--) {
            InvestTransactionDTO investDTO = new InvestTransactionDTO();

            JsonObject investJson = (JsonObject) trans_list.get(i);
            investDTO.setTrans_amt(getString(String.valueOf(investJson.get("trans_amt"))));
            investDTO.setTrans_type(getString(String.valueOf(investJson.get("trans_type"))));
            investDTO.setTrans_type_detail(getString(String.valueOf(investJson.get("trans_type_detail"))));
            investDTO.setSettle_amt(getString(String.valueOf(investJson.get("settle_amt"))));
            investDTO.setTrans_num(getString(String.valueOf(investJson.get("trans_num"))));

            investDTO.setBalance_amt(getString(String.valueOf(investJson.get("balance_amt"))));
            investDTO.setProd_code(getString(String.valueOf(investJson.get("prod_code"))));
            investDTO.setProd_name(getString(String.valueOf(investJson.get("prod_name"))));
            investDTO.setBase_amt(getString(String.valueOf(investJson.get("base_amt"))));
            Date dtime = dtimeFormat.parse(getString(String.valueOf(investJson.get("trans_dtime"))));
            investDTO.setTrans_dtime(dtime);
            investDTO.setCurrency_code(getString(String.valueOf(investJson.get("currency_code"))));

            transactionList.add(investDTO);
        }

//        System.out.println("transList = " + transList);
        model.addAttribute("accountNum", account);
        model.addAttribute("transList", transactionList);
        System.out.println("model = " + model);

        return "/app/investDetail";
    }


    // ### 보험 ###
    @PostMapping("/app/insu/myAccount")
    @ResponseBody
    public JsonObject myInsuAccount(String clientNum, String industry) {
        JsonObject object = new JsonObject();
        String uri_1 = "/insurances";
        List<GetDataDTO> list1 = getDataService.selectAllIndustry(clientNum, uri_1, industry);
        System.out.println("list1 = " + list1);
        System.out.println("list1.isEmpty() = " + list1.isEmpty());

        JsonArray array = new JsonArray();
        for (GetDataDTO g : list1) {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject) parser.parse(g.getResponseData());
            JsonObject jsonObject1 = (JsonObject) parser.parse(g.getRequestData());

            array.add(jsonObject1);
            array.add(jsonObject);
        }

        object.addProperty("insuList", array.toString());

        return object;
    }


    // ### 인증 페이지 ###
    // ( 가입상품 목록 전송 요구서 )
    @GetMapping("/app/certificationSendReq/{list}")
    public String certificationSendReq(Model model, @PathVariable List<String> list, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        System.out.println("list = " + list);
        model.addAttribute("list", list);

        // 선택한  org_code 넣을 lIst
        List<String> codeList = new ArrayList<>();

        List<MdAgencyDTO> agencyList = mydataService.mdAgencySelectAll();

        for (MdAgencyDTO m : agencyList) {
            for (String str : list) {
                if (m.getName().equals(str)) {
                    codeList.add(m.getCode());
                }
            }
        }

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("choiceAgency", codeList);

        System.out.println("codeList = " + codeList);
        System.out.println("agencyList = " + agencyList);

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
    public String addProperty(Model model, HttpSession session, @AuthenticationPrincipal UserCustomDetails userDetails) {
        /* add code */
        // 1. 진짜토근일때
        // 2. 가짜 토큰일때

        // 선택한 기관들
        List<String> agencyList = (List<String>) session.getAttribute("choiceAgency");


        if (agencyList == null) {
            return "redirect:/app/main";
        }
        System.out.println("agencyList = " + agencyList);
        JsonObject responseObj = new JsonObject();
        JsonObject requestObj = new JsonObject();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = new UserDTO();

        if (principal instanceof UserDetails) {
            //일반로그인
            String username = ((UserDetails) principal).getUsername();
            System.out.println("username 1 = " + username);
            System.out.println((UserDetails) principal);
            userDTO = userDetails.getUserDTO();
        } else {
            //인젠트 로그인
            UserDTO logIn = (UserDTO) session.getAttribute("logIn");
            System.out.println("==============" + logIn);
            String username = principal.toString();
            System.out.println("username 2  = " + username);
            System.out.println("userinfo 2  " + principal);
            userDTO = logIn;
        }
        if (agencyList.get(0).equals("INVEST0009")) {
            System.out.println("===========================================JSONOBJECT");
            String token = userDTO.getToken();

            responseObj = mydataApiService.invest001(agencyList.get(0), "100", token);

            /* request data Json 형태 */
            requestObj.addProperty("org_code", agencyList.get(0));
            requestObj.addProperty("limit", "100");

            System.out.println("jsonObject = " + responseObj);
            System.out.println("requestObj = " + requestObj);

        }
        List<MdAgencyDTO> agencyDTOList = mydataService.mdAgencySelectAll();

        String clientNum = userDTO.getOwnNum();

        String industry = "";


        // 업권 확인
        for (MdAgencyDTO m : agencyDTOList) {
            // agencyList는 다 같은 업권이므로 아무 하나만 골라 업권 찾기
            if (m.getCode().equals(agencyList.get(0))) {
                industry = m.getIndustry();
            }
        }

        // 계좌정보 가져오기


        /* 요청 데이터에 맞는 계좌 정보 호출 후 넣을 LIST*/
        String uri_1 = "/accounts";
        List<GetDataDTO> list = new ArrayList<>();

        List<AddPropertyDTO> accountList = new ArrayList<>();

        if (industry.equals("bank")) {
            // 자산연결 선택한 기관 돌고
            for (String str : agencyList) {
                // 각 은행 마다 돌고
                for (int i = 0; i < list.size(); i++) {
                    JsonObject object = (JsonObject) JsonParser.parseString(list.get(i).getRequestData());
                    String org_code = String.valueOf(object.get("org_code"));
                    String org = getString(org_code);
                    if (str.equals(org)) {
                        JsonObject resObj = (JsonObject) JsonParser.parseString(list.get(i).getResponseData());
                        System.out.println("resObj = " + resObj);
                        JsonArray array = resObj.get("account_list").getAsJsonArray();
                        // 계좌 마다 돌고
                        for (int j = 0; j < array.size(); j++) {
                            AddPropertyDTO addPropertyDTO = new AddPropertyDTO();

                            JsonObject tempJson = (JsonObject) array.get(j);
                            addPropertyDTO.setOrg_code(org);
                            addPropertyDTO.setProd_name(getString(String.valueOf(tempJson.get("prod_name"))));
                            addPropertyDTO.setAccount_num(getString(String.valueOf(tempJson.get("account_num"))));

                            accountList.add(addPropertyDTO);
                        }

                    }
                }
            }
        } else if (industry.equals("invest")) {

            GetDataDTO temp = new GetDataDTO();
            temp.setUri(uri_1);
            temp.setClientNum(userDTO.getOwnNum());
            temp.setIndustry(industry);
            temp.setApiId("금투-001");
            String reqData = requestObj.toString();
            temp.setRequestData(reqData);
            String resData = responseObj.toString();
            temp.setResponseData(resData);
            list.add(temp);


            // 자산연결 선택한 기관 돌고
            for (String str : agencyList) {
                // 각 은행 마다 돌고
                for (int i = 0; i < list.size(); i++) {
                    JsonObject object = (JsonObject) JsonParser.parseString(list.get(i).getRequestData());
                    String org_code = String.valueOf(object.get("org_code"));
                    String org = getString(org_code);
                    if (str.equals(org)) {
                        JsonObject resObj = (JsonObject) JsonParser.parseString(list.get(i).getResponseData());
                        System.out.println("resObj = " + resObj);
                        JsonArray array = resObj.get("account_list").getAsJsonArray();

                        // 계좌 마다 돌고
                        for (int j = 0; j < array.size(); j++) {
                            AddPropertyDTO addPropertyDTO = new AddPropertyDTO();

                            JsonObject tempJson = (JsonObject) array.get(j);
                            addPropertyDTO.setOrg_code(org);
                            addPropertyDTO.setProd_name(getString(String.valueOf(tempJson.get("account_name"))));
                            addPropertyDTO.setAccount_num(getString(String.valueOf(tempJson.get("account_num"))));

                            accountList.add(addPropertyDTO);
                        }

                    }
                }
            }
        }


        System.out.println("accountList = " + accountList);

        // 선택한 기관들 정보 가지고 있을 List
        List<MdAgencyDTO> choiceAgency = new ArrayList<>();

        for (AddPropertyDTO a : accountList) {
            for (MdAgencyDTO m : agencyDTOList) {
                if (a.getOrg_code().equals(m.getCode())) {
                    choiceAgency.add(m);
                }
            }
        }
        System.out.println("choiceAgency = " + choiceAgency);
        model.addAttribute("industry", industry);
        System.out.println("industry = " + industry);

        // 중복 제거
        List<MdAgencyDTO> mdAgencyDTOList = choiceAgency.stream().distinct().collect(Collectors.toList());

        System.out.println("mdAgencyDTOList = " + mdAgencyDTOList);

        model.addAttribute("accountList", accountList);

        model.addAttribute("mdAgencyDTOList", mdAgencyDTOList);

        return "/app/addProperty";
    }

    @ResponseBody
    @PostMapping("/app/connectAgency")
    public void connectAgency(@RequestParam(value = "accountList[]") ArrayList<String> accountList, @RequestParam(value = "org_code_list[]") ArrayList<String> orgList, @AuthenticationPrincipal UserCustomDetails userDetails, HttpSession session) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = new UserDTO();

        if (principal instanceof UserDetails) {
            //일반로그인
            String username = ((UserDetails) principal).getUsername();
            System.out.println("username 1 = " + username);
            System.out.println((UserDetails) principal);
            userDTO = userDetails.getUserDTO();
        } else {
            //인젠트 로그인
            UserDTO logIn = (UserDTO) session.getAttribute("logIn");
            System.out.println("==============" + logIn);
            String username = principal.toString();
            System.out.println("username 2  = " + username);
            System.out.println("userinfo 2  " + principal);
            userDTO = logIn;
        }

        System.out.println("accountList = " + accountList); // 계좌번호
        System.out.println("orgList = " + orgList); // 기관 코드

        /*invest001을 여기서 적재를 하는데 조건은 위의 accountList 랑 invest001을 호출해서 얻어온 account_num 의 데이터가 같으면 진행 */
        JsonObject responseObj = mydataApiService.invest001(orgList.get(0), "100", userDTO.getToken());
        JsonArray jsonArray = (JsonArray) responseObj.get("account_list");
        JsonObject jsonObject = (JsonObject) jsonArray.get(0);
        String choiceAccountNum = accountList.get(0).replaceAll("\"", "");
        String accountNum = jsonObject.get("account_num").toString().replaceAll("\"", "");
        JsonObject requestObj = new JsonObject();
        /* request data Json 형태 */
        requestObj.addProperty("org_code", orgList.get(0));
        requestObj.addProperty("limit", "100");


        /* 여기서 시작! */
        JsonObject body = new JsonObject();
        if (!orgList.get(0).isEmpty()) { // org 코드값이 존재한다면

            /* 금투-001 insert */
            if (choiceAccountNum.equals(accountNum)) {
                System.out.println("AppController.connectAgency");

                GetDataDTO getDataDTO = new GetDataDTO();
                getDataDTO.setRequestData(requestObj.toString());
                getDataDTO.setResponseData(responseObj.toString());
                getDataDTO.setUri("/accounts");
                getDataDTO.setIndustry("invest");
                getDataDTO.setApiId("금투-001");
                getDataDTO.setClientNum(userDTO.getOwnNum());
                mydataApiService.insertResult(getDataDTO); // 금투 123 모두 들어갈 수 있음
            }

            body.addProperty("org_code", orgList.get(0));
            body.addProperty("account_num", accountList.get(0) + " ");
            body.addProperty("search_timestamp", "0");


            System.out.println();
            System.out.println(mydataApiService.invest002(body.toString(), userDTO.getToken()));
            GetDataDTO getDataDTO = new GetDataDTO();
            getDataDTO.setUri("/accounts/basic");
            getDataDTO.setResponseData(mydataApiService.invest002(body.toString(), userDTO.getToken()).toString());
            getDataDTO.setIndustry("invest");
            getDataDTO.setApiId("금투-002");

            body.addProperty("account_num", accountList.get(0));
            getDataDTO.setRequestData(body.toString());

            getDataDTO.setClientNum(userDTO.getOwnNum());
            /* 금투-002 insert  */
            mydataApiService.insertResult(getDataDTO);

            if (!orgList.get(0).isEmpty()) { // org 코드값이 존재한다면
                body.addProperty("from_date", "20211017");
                body.addProperty("to_date", "20221017");
                body.addProperty("next_page", "");
                body.addProperty("limit", "100");
            }

            System.out.println();
            System.out.println("금투003");
            body.addProperty("account_num", accountList.get(0) + " ");
            System.out.println(mydataApiService.invest003(body.toString(), userDTO.getToken()));

            getDataDTO.setResponseData(mydataApiService.invest003(body.toString(), userDTO.getToken()).toString());
            getDataDTO.setUri("/accounts/transactions");
            getDataDTO.setApiId("금투-003");
//            String data = body.get("account_num").toString().replaceAll(" ", "");
            body.addProperty("account_num", accountList.get(0));
            getDataDTO.setRequestData(body.toString());
            mydataApiService.insertResult(getDataDTO);
            System.out.println("적재 완료오 !!! ");
//        return "redirect:/app/main";
        }
    }


    @GetMapping("/app/sendReq")
    public String sendReq(Model model, HttpSession session, @AuthenticationPrincipal UserCustomDetails userDetails) {
        List<String> agencyList = (List<String>) session.getAttribute("choiceAgency");
        if (agencyList == null) {
            return "redirect:/app/main";
        }
        System.out.println("agencyList = " + agencyList);

        List<MdAgencyDTO> agencyDTOList = mydataService.mdAgencySelectAll();

        List<String> list = new ArrayList<>();

        for (String str : agencyList) {
            for (MdAgencyDTO m : agencyDTOList) {
                if (str.equals(m.getCode())) {
                    list.add(m.getName());
                }
            }
        }

        model.addAttribute("agencyList", list);

        return "/app/sendReq";
    }

    @ResponseBody
    @PostMapping("/app/deleteAgency")
    public void deleteAgency(@RequestParam(value = "choiceAgency[]") ArrayList<String> choiceAgency, String industry, @AuthenticationPrincipal UserCustomDetails userDetails, HttpSession session) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = new UserDTO();

        if (principal instanceof UserDetails) {
            //일반로그인
            String username = ((UserDetails) principal).getUsername();
            System.out.println("username 1 = " + username);
            System.out.println((UserDetails) principal);
            userDTO = userDetails.getUserDTO();
        } else {
            //인젠트 로그인
            UserDTO logIn = (UserDTO) session.getAttribute("logIn");
            System.out.println("==============" + logIn);
            String username = principal.toString();
            System.out.println("username 2  = " + username);
            System.out.println("userinfo 2  " + principal);
            userDTO = logIn;
        }

        System.out.println("industry = " + industry);

        System.out.println("choiceAgency = " + choiceAgency);

        for (int i = 0; i < choiceAgency.size(); i++) {
            // 삭제 일단 막아놓음
            System.out.println("choiceAgency.get(i) = " + choiceAgency.get(i));
            getDataService.deleteAccount(choiceAgency.get(i), userDTO.getOwnNum(), industry);
        }
    }
}
