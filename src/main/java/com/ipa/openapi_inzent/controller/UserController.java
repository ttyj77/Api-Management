package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonObject;
import com.ipa.openapi_inzent.model.UserDTO;
import com.ipa.openapi_inzent.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "login";
    }

    @PostMapping("/register")
    public String register(UserDTO userDTO) {
        userService.register(userDTO);
        return "redirect:/";
    }

    @GetMapping("/overlapCheck")
    @ResponseBody
    public JsonObject idCheck(String username , String nickname) {
        JsonObject object = new JsonObject();
        UserDTO un = userService.findByUsername(username);
        UserDTO nn = userService.findByNickname(nickname);
        if (un != null) {
            object.addProperty("username", true);
        } else {
            object.addProperty("username", false);
        }

        if (nn != null) {
            object.addProperty("nickname", true);
        } else {
            object.addProperty("nickname", false);
        }
        return object;
    }
}
