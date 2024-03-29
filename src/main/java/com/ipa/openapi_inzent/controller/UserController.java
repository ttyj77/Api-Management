package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ipa.openapi_inzent.config.auth.UserCustomDetails;
import com.ipa.openapi_inzent.model.RequestDTO;
import com.ipa.openapi_inzent.model.UserDTO;
import com.ipa.openapi_inzent.model.UserRoleDTO;
import com.ipa.openapi_inzent.service.RequestService;
import com.ipa.openapi_inzent.service.RoleService;
import com.ipa.openapi_inzent.service.UserService;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private RequestService requestService;
    private RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RequestService requestService, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userService = userService;
        this.requestService = requestService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "/login";
    }

    @GetMapping("/register")
    public String register() {
        return "/login";
    }

    @PostMapping("/register")
    public String register(UserDTO userDTO) {
        // 회원 리스트에 넣기
        int id = userService.register(userDTO);

        // 역할 넣기 (일반 사용자 2)
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(id);
        userRoleDTO.setRoleId(2); // 일반 사용자 ( ROLE_NORMAL )
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
        if (userService.findByUsername(username).isEmpty()) {
            object.addProperty("username", false);
        } else {
            object.addProperty("username", true);
        }

        UserDTO nn = userService.findByNickname(nickname);
        if (nn != null) {
            object.addProperty("nickname", true);
        } else {
            object.addProperty("nickname", false);
        }
        return object;
    }

    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal UserCustomDetails userCustomDetails, Model model, HttpSession session) {

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

        model.addAttribute("user", userDTO);
        return "/mypage";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, HttpSession session) {
        UserDTO logIn = (UserDTO) session.getAttribute("logIn");
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

        object.addProperty("username", userDTO.getUsername());
        object.addProperty("nickname", userDTO.getNickname());
        object.addProperty("email", userDTO.getEmail());
        object.addProperty("activate", userDTO.isActivate());

        return object;
    }

    // 계정관리 비번 변경
    @PostMapping("/updatePwd/{id}")
    public String updatePwd(@PathVariable int id, String password) {
        UserDTO userDTO = userService.userOne(id);

        userDTO.setPassword(passwordEncoder.encode(password));

        userService.update(userDTO);

        return "redirect:/accountList";
    }

    // 내정보 비번 변경
    @PostMapping("/updatePw/{id}")
    public String updatePw(@PathVariable int id, String password) {
        UserDTO userDTO = userService.userOne(id);

        userDTO.setPassword(passwordEncoder.encode(password));

        userService.update(userDTO);

        return "redirect:/user/mypage";
    }

    @GetMapping("/selectOneRole")
    @ResponseBody
    public JsonObject selectOneRole(int userId) {
        JsonObject object = new JsonObject();
        List<UserDTO> userRoleList = userService.selectOne(userId);
        JsonArray selectRoleArray = new JsonArray();
        for (UserDTO role : userRoleList) {
            JsonObject r = new JsonObject();
            r.addProperty("id", role.getRoleId());
            r.addProperty("code", role.getCode());
            r.addProperty("name", role.getName());
            selectRoleArray.add(r);
        }
        object.addProperty("selectedRoleList", selectRoleArray.toString());
        return object;
    }

    @PostMapping("/updateAccount")
    public String updateAccount(int id, String nickname, String email, String[] roleId) {
        // 유저 정보 갱신
        UserDTO temp = userService.userOne(id);
        temp.setNickname(nickname);
        temp.setEmail(email);

        userService.update(temp);

        // 유저 role 정보 갱신
        userService.deleteRole(id); // 기존 role 다 삭제하고
        for (int i = 0; i < roleId.length; i++) {
            UserRoleDTO userRoleDTO = new UserRoleDTO();
            userRoleDTO.setUserId(id);
            userRoleDTO.setRoleId(Integer.parseInt(roleId[i]));
            userService.insertRole(userRoleDTO);
        }

        return "redirect:/accountList";
    }

    // mypage role 없이 계정 update
    @PostMapping("/updateUser")
    public String updateUser(Model model, int id, String nickname, String email) {

        // 유저 정보 갱신
        UserDTO temp = userService.userOne(id);
        temp.setNickname(nickname);
        temp.setEmail(email);

        userService.update(temp);

        model.addAttribute("user", temp);

        return "/mypage";
    }

    @GetMapping("/oauth2Login")
    public String oauth2Login() {
        return "/oauth2/inzent_login";
    }

}
