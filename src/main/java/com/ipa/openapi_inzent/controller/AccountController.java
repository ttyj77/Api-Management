package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ipa.openapi_inzent.model.UserDTO;
import com.ipa.openapi_inzent.service.RoleService;
import com.ipa.openapi_inzent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AccountController {
    UserService userService;
    RoleService roleService;

    @Autowired
    public AccountController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/accountList")
    public String accountList(Model model) {
        model.addAttribute("list", userService.selectAll());
        model.addAttribute("userList", userService.userList());
        model.addAttribute("roleList", roleService.selectAll());
        return "/accountList";
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

    @GetMapping("/accountSearch")
    @ResponseBody
    public JsonObject requestSearch(String keyword) {
        JsonObject object = new JsonObject();

        List<UserDTO> list = userService.accountListSearch(keyword);
        System.out.println("keyword = " + keyword);
        System.out.println("list = " + list);

        JsonArray userArr = new JsonArray();

        for (UserDTO userDTO : list) {
            if (userDTO.isApprove()) {
                JsonObject arr = new JsonObject();
                arr.addProperty("id", userDTO.getId());
                arr.addProperty("username", userDTO.getUsername());
                arr.addProperty("nickname", userDTO.getNickname());
                arr.addProperty("email", userDTO.getEmail());
                arr.addProperty("activate", userDTO.isActivate());
                arr.addProperty("approve", userDTO.isApprove());
                userArr.add(arr);
            }
        }
        object.addProperty("userList", userArr.toString());

        System.out.println("object = " + object);

        return object;
    }

    // 활성화 비활성화 버튼 누를시
    @GetMapping("/showActivate")
    @ResponseBody
    public JsonObject showActivate(String activate) {
        JsonObject object = new JsonObject();
        List<UserDTO> wholeList = userService.userList();
        List<UserDTO> tfList = userService.choiceActivate(Boolean.parseBoolean(activate));
//        System.out.println("wholeList = " + wholeList);
//        System.out.println("tfList = " + tfList);
        System.out.println("activate = " + activate);
        if (activate.equals("whole")) {
            System.out.println(" whole 조건 들어옴");
            JsonArray userArr = new JsonArray();
            for (UserDTO userDTO : wholeList) {
                if (userDTO.isApprove()) {
                    JsonObject arr = new JsonObject();
                    arr.addProperty("id", userDTO.getId());
                    arr.addProperty("username", userDTO.getUsername());
                    arr.addProperty("nickname", userDTO.getNickname());
                    arr.addProperty("email", userDTO.getEmail());
                    arr.addProperty("activate", userDTO.isActivate());
                    arr.addProperty("approve", userDTO.isApprove());
                    userArr.add(arr);
                }
            }
            object.addProperty("userList", userArr.toString());
        } else {
            System.out.println(" else 조건 들어옴");
            JsonArray userArr = new JsonArray();
            for (UserDTO userDTO : tfList) {
                if (userDTO.isApprove()) {
                    JsonObject arr = new JsonObject();
                    arr.addProperty("id", userDTO.getId());
                    arr.addProperty("username", userDTO.getUsername());
                    arr.addProperty("nickname", userDTO.getNickname());
                    arr.addProperty("email", userDTO.getEmail());
                    arr.addProperty("activate", userDTO.isActivate());
                    arr.addProperty("approve", userDTO.isApprove());
                    userArr.add(arr);
                }
            }
            object.addProperty("userList", userArr.toString());
        }
        return object;
    }
}