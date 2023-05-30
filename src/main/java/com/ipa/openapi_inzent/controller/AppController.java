package com.ipa.openapi_inzent.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AppController {

    @GetMapping("/app/login")
    public String login() {
        return "applogin";
    }

    @GetMapping("/app/main")
    public String main() {
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
}
