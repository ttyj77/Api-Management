package com.ipa.openapi_inzent.controller;

import com.ipa.openapi_inzent.model.RoleDTO;
import com.ipa.openapi_inzent.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class authorizationController {

    RoleService roleService;

    @Autowired
    public authorizationController(RoleService roleService) {
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

        return "authorization";
    }

    // 권한관리 역할 삭제
    @GetMapping("/roleDelete/{id}")
    public String roleDelete(@PathVariable int id) {
        System.out.println("authorizationController.roleDelete");
//        roleService.deleteRole(id);

        return "redirect:/authorization";
    }
}
