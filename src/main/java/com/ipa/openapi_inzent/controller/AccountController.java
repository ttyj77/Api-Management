package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonObject;
import com.ipa.openapi_inzent.model.UserDTO;
import com.ipa.openapi_inzent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AccountController {
    UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/accountList")
    public String accountList(Model model) {
        model.addAttribute("list", userService.selectAll());
        model.addAttribute("userList", userService.userList());
        return "accountList";
    }

    @PostMapping("/turnActivate/{id}")
    @ResponseBody
    public void turnActivate(@PathVariable int id) {
        UserDTO userDTO = userService.userOne(id);
        System.out.println("userDTO.isActivate() before = " + userDTO.isActivate());
        userDTO.setActivate(!userDTO.isActivate());
        System.out.println("userDTO.isActivate() after = " + userDTO.isActivate());
        userService.update(userDTO);
    }
}
