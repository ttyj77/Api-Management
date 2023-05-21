package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonObject;
import com.ipa.openapi_inzent.model.RequestDTO;
import com.ipa.openapi_inzent.model.UserDTO;
import com.ipa.openapi_inzent.service.RequestService;
import com.ipa.openapi_inzent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private RequestService requestService;

    @Autowired
    public UserController(UserService userService, RequestService requestService) {
        this.userService = userService;
        this.requestService = requestService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "login";
    }

    @PostMapping("/register")
    public String register(UserDTO userDTO) {
        userDTO.setRole("ROLE_USER");
        // 회원 리스트에 넣기
        int id = userService.register(userDTO);

        RequestDTO requestDTO = new RequestDTO();

        String title = "회원가입 승인 요청";
        String content = "사용자 아이디:" + userDTO.getUsername() + " / 사용자 이름:" + userDTO.getNickname()
                + " / 사용자 이메일:" + userDTO.getEmail();

        requestDTO.setReqUsername(userDTO.getUsername());
        requestDTO.setReqNickname(userDTO.getNickname());
        requestDTO.setUserId(id);
        requestDTO.setTitle(title);
        requestDTO.setContent(content);

        // 요청 리스트에 넣기
        requestService.insert(requestDTO);

        return "redirect:/";
    }

    @GetMapping("/overlapCheck")
    @ResponseBody
    public JsonObject idCheck(String username, String nickname) {
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

    @GetMapping("/mypage/{id}")
    public String mypage(Model model, @PathVariable int id) {
        UserDTO userDTO = userService.selectOne(id);

        model.addAttribute("user", userDTO);
        return "mypage";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        System.out.println("UserController.delete");
        userService.delete(id);
        return "redirect:/";
    }

}
