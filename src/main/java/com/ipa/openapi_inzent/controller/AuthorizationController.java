package com.ipa.openapi_inzent.controller;

import com.ipa.openapi_inzent.config.auth.UserCustomDetails;
import com.ipa.openapi_inzent.model.RoleDTO;
import com.ipa.openapi_inzent.model.UserDTO;
import com.ipa.openapi_inzent.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthorizationController {

    RoleService roleService;

    @Autowired
    public AuthorizationController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/authorization")
    public String authorization(Model model) {
        List<RoleDTO> roleList = roleService.selectAll();
        List<RoleDTO> temp = new ArrayList<>();
        for (RoleDTO role : roleList) {
            // ROLE_ADMIN 을 ADMIN으로 잘라줌
            role.setCode(role.getCode().replace("ROLE_", ""));
            temp.add(role);
        }
        model.addAttribute("roleList", temp);

        return "/authorization";
    }

    // 권한관리 역할 삭제
    @GetMapping("/roleDelete/{id}")
    public String roleDelete(@PathVariable int id) {
        roleService.deleteRole(id);
        return "redirect:/authorization";
    }

    /* APP 인젠트 인증 페이지*/
    @GetMapping("/app/authorization")
    public String inzentAuthorization(Model model, @AuthenticationPrincipal UserCustomDetails userCustomDetails, HttpSession session) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = new UserDTO();


        if (principal instanceof UserDetails) {
            //일반로그인
            String username = ((UserDetails) principal).getUsername();
            userDTO = userCustomDetails.getUserDTO();
        } else {
            //인젠트 로그인
            UserDTO logIn = (UserDTO) session.getAttribute("logIn");
            String username = principal.toString();
            userDTO = logIn;
        }

        if (userDTO.getExpireDate() == null) {
            return "redirect:/app/main";
        }

        model.addAttribute("user", userDTO);

        return "/app/inzentAuthorization";
    }
}
