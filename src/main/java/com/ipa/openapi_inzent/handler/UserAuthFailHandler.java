package com.ipa.openapi_inzent.handler;


import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public class UserAuthFailHandler extends SimpleUrlAuthenticationFailureHandler {
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        System.out.println("failure handler in");

        String errorMessage = null;

        if (e instanceof BadCredentialsException) {
//            httpServletRequest.setAttribute("LoginFailMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
            System.out.println("아이디 틀렸음 exception");
            errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다.";
        } else if (e instanceof DisabledException) {
            errorMessage = "본 계정은 현재 비활성화 되어 있습니다. 관리자에게 문의하세요.";
//            httpServletRequest.setAttribute("LoginFailMessage", "본 계정은 현재 비활성화 되어 있습니다. 관리자에게 문의하세요.");
        } else if (e instanceof InternalAuthenticationServiceException) { //존재하지 않는 아이디일 때 던지는 예외
            errorMessage = "아이디가 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
        } else if (e instanceof UsernameNotFoundException) {
            errorMessage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
        } else if (e instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
        } else {
            errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";
        }

        e.printStackTrace();

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
        setDefaultFailureUrl("/user/login?error=true&exception=" + errorMessage);
        super.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
        System.out.println("failure handler out");


//        httpServletResponse.sendRedirect("/login");
//        RequestDispatcher dispatcher = httpServletRequest.getRequestDispatcher("/login");
//        dispatcher.forward(httpServletRequest, httpServletResponse);
    }
}
