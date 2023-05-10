package com.ipa.openapi_inzent.controller;

import com.ipa.openapi_inzent.model.RoleDTO;
import com.ipa.openapi_inzent.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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

    @GetMapping("/selectAll")
    @ResponseBody
    public List<RoleDTO> selectALl() {
        List<RoleDTO> list = roleService.selectAll();
        System.out.println("list = " + list);
        return list;
    }


}
