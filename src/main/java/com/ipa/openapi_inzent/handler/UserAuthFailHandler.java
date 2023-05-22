package com.ipa.openapi_inzent.handler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;

public class UserAuthFailHandler extends SimpleUrlAuthenticationFailureHandler {
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        System.out.println("failure handler in");

        String errorMessage = null;

        if (e instanceof BadCredentialsException) {
//            httpServletRequest.setAttribute("LoginFailMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
            errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다.";
        } else if (e instanceof DisabledException) {
            errorMessage = "본 계정은 현재 비활성화 되어 있습니다. 관리자에게 문의하세요.";
//            httpServletRequest.setAttribute("LoginFailMessage", "본 계정은 현재 비활성화 되어 있습니다. 관리자에게 문의하세요.");
        }
        e.printStackTrace();

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
        setDefaultFailureUrl("/user/login?error=true&exception="+errorMessage);
        super.onAuthenticationFailure(httpServletRequest,httpServletResponse, e);

//        httpServletResponse.sendRedirect("/login");
//        RequestDispatcher dispatcher = httpServletRequest.getRequestDispatcher("/login");
//        dispatcher.forward(httpServletRequest, httpServletResponse);
    }
}
