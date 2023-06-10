package com.ipa.openapi_inzent.controller;

import com.ipa.openapi_inzent.config.auth.UserCustomDetails;
import com.ipa.openapi_inzent.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class AppController {

    UserService userService;

    @Autowired
    public AppController(UserService userService) {
        this.userService = userService;
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


    // ### 투자 ###


    // ### 보험 ###


    // ### 통신 ###
}
