package com.ipa.openapi_inzent.controller;

import com.ipa.openapi_inzent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        System.out.println("userService = " + userService.selectAll());
        return "accountList";
    }
}
