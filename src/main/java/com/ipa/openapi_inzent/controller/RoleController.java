package com.ipa.openapi_inzent.controller;

import com.ipa.openapi_inzent.model.RoleDTO;
import com.ipa.openapi_inzent.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/role")
public class RoleController {
    RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping()
    public void insert(RoleDTO roleDTO) {
        roleService.insert(roleDTO);
    }

    public void selectALl(Model model) {
        model.addAttribute("list",roleService.selectAll());
    }

}
