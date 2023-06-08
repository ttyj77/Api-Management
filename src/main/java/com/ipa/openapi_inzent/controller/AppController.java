//package com.ipa.openapi_inzent.controller;
//
//import com.ipa.openapi_inzent.config.auth.UserCustomDetails;
//import com.ipa.openapi_inzent.service.UserService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.ui.Model;
//
////import javax.servlet.http.HttpServletRequest;
////import javax.servlet.http.HttpServletResponse;
//
//@Controller
//public class AppController {
//
//    UserService userService;
//
//    @Autowired
//    public AppController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("/app/login")
//    public String login() {
//        return "applogin";
//    }
//
//    @GetMapping("/app/main")
//    public String main(Model model,@AuthenticationPrincipal UserCustomDetails userDetails) {
//        System.out.println("userDetails = " + userDetails);
//        System.out.println("userDetails = " + userDetails.getUserDTO());
//        model.addAttribute("user", userDetails.getUserDTO());
//        return "/app/main";
//    }
//
//    // 인증을 받은 사용자가 로그아웃가능 로그아웃은 SecurityContextLogoutHandler이친구가 진행함
//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // authentication 이 널이 아니면 로그아웃진행
//        if (authentication != null) {
//            new SecurityContextLogoutHandler().logout(request, response, authentication);
//        }
//        return "redirect:/";
//    }
//
//    @GetMapping("/app/agencyChoice")
//    public String agencyChoice() {
//
//        return "/app/agencyChoice";
//    }
//
//    // ### 카드 ###
//    @GetMapping("/app/cardDetail")
//    public String cardDetail() {
//        //파라미터들 있어야함 ( 카드 정보 )
//        return "/app/cardDetail";
//    }
//
//    // 추가
//    @GetMapping("/app/card/insert")
//    public String cardInsert() {
//        return "/app/cardInsert";
//    }
//
//
//    // ### 은행 ###
//    @GetMapping("/app/bank/insert")
//    public String bank() {
//
//        return "/app/bankInsert";
//    }
//
//
//    // ### 투자 ###
//
//
//
//    // ### 보험 ###
//
//
//    // ### 통신 ###
//}
