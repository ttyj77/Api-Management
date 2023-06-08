package com.ipa.openapi_inzent.handler;


import com.ipa.openapi_inzent.config.auth.UserCustomDetails;
import com.ipa.openapi_inzent.model.UserDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserAuthSuccessHandler implements AuthenticationSuccessHandler {
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        System.out.println("success handler in");
        UserDTO userDTO = ((UserCustomDetails) authentication.getPrincipal()).getUserDTO();
        // 계정 정지 => 회원가입 요청 상태 + 거절일때
        if (!userDTO.isActivate()) {
            throw new DisabledException("본 계정은 현재 비활성화 되어 있습니다.");
        }
        userDTO.setPassword(null);

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("logIn", userDTO);

        httpServletResponse.sendRedirect("/api");
    }
}
