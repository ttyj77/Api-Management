package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonObject;
import com.ipa.openapi_inzent.model.RequestDTO;
import com.ipa.openapi_inzent.model.UserDTO;
import com.ipa.openapi_inzent.model.UserRoleDTO;
import com.ipa.openapi_inzent.service.RequestService;
import com.ipa.openapi_inzent.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private RequestService requestService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RequestService requestService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.requestService = requestService;
        this.passwordEncoder = passwordEncoder;
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
        // 회원 리스트에 넣기
        int id = userService.register(userDTO);

        // 역할 넣기 (일반 사용자 2)
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(id);
        userRoleDTO.setRoleId(2); // 일반 사용자 ( ROLE_USER )
        userService.insertRole(userRoleDTO);

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
    public String delete(@PathVariable int id, HttpSession session) {
        UserDTO logIn = (UserDTO) session.getAttribute("logIn");
        System.out.println("UserController.delete");
        userService.delete(id);
        if (logIn.getId() == id) {
            // 회원탈퇴
            return "redirect:/";
        } else {
            // 강제탈퇴
            return "redirect:/accountList";
        }
    }

    @GetMapping("/userOne/{id}")
    @ResponseBody
    public JsonObject userOne(@PathVariable int id) {
        JsonObject object = new JsonObject();
        UserDTO userDTO = userService.userOne(id);
        List<UserRoleDTO> list = userService.userRoles(id);
        System.out.println("list = " + list);
        System.out.println("userDTO = " + userDTO);
        object.addProperty("username", userDTO.getUsername());
        object.addProperty("nickname", userDTO.getNickname());
        object.addProperty("email", userDTO.getEmail());
        object.addProperty("activate", userDTO.isActivate());

        return object;
    }

    @PostMapping("updatePwd/{id}")
    public String updatePw(@PathVariable int id, String password) {
        System.out.println("password = " + password);
        System.out.println("id = " + id);
        UserDTO userDTO = userService.userOne(id);
        System.out.println("userDTO = " + userDTO);

        userDTO.setPassword(passwordEncoder.encode(password));

        userService.update(userDTO);

        return "redirect:/accountList";
    }

}
