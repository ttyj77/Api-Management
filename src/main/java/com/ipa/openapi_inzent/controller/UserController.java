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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

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

    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal UserCustomDetails userDetails, Model model) {
        System.out.println("userDetails");
        System.out.println(userDetails.getUsername());
        System.out.println(userDetails.getUserDTO());
        System.out.println(userDetails.getUserDTO().getId());

        model.addAttribute("user", userDetails.getUserDTO());
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

        object.addProperty("username", userDTO.getUsername());
        object.addProperty("nickname", userDTO.getNickname());
        object.addProperty("email", userDTO.getEmail());
        object.addProperty("activate", userDTO.isActivate());

        return object;
    }

    @PostMapping("/updatePwd/{id}")
    public String updatePw(@PathVariable int id, String password) {
        UserDTO userDTO = userService.userOne(id);

        userDTO.setPassword(passwordEncoder.encode(password));

        userService.update(userDTO);

        return "redirect:/accountList";
    }

    @GetMapping("/selectOneRole")
    @ResponseBody
    public JsonObject selectOneRole(int userId) {
        JsonObject object = new JsonObject();
        List<UserDTO> userRoleList = userService.selectOne(userId);
        System.out.println("userRoleList = " + userRoleList);
        JsonArray selectRoleArray = new JsonArray();
        for (UserDTO role : userRoleList) {
            JsonObject r = new JsonObject();
            r.addProperty("id", role.getRoleId());
            r.addProperty("code", role.getCode());
            r.addProperty("name", role.getName());
            selectRoleArray.add(r);
        }
        object.addProperty("selectedRoleList", selectRoleArray.toString());
        System.out.println("selectRoleArray = " + selectRoleArray);
        return object;
    }

    @PostMapping("/updateAccount")
    public String updateAccount(int id, String nickname, String email, String[] roleId) {

        System.out.println("UserController.updateAccount");
        System.out.println("id = " + id);
        System.out.println("nickname = " + nickname);
        System.out.println("email = " + email);

        for (String role : roleId) {
            System.out.println("role = " + role);
        }
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

        System.out.println(id + "의 갱신한 역할들" + userService.userRoles(id));

        return "redirect:/accountList";
    }


//    @PostMapping("updatePw/{id}")
//    public void updatePw(Model model, RedirectAttributes redirectAttributes, @PathVariable int id, String oldPw, String newPw, String newPw2) {
//        UserDTO userDTO = userService.selectOne(id);
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//        if (!passwordEncoder.matches(oldPw, userDTO.getPassword())) {
//            redirectAttributes.addFlashAttribute("message", "기존 비밀번호를 확인해주세요.");
//        } else if (!newPw.equals(newPw2)) {
//            redirectAttributes.addFlashAttribute("message", "새 비밀번호가 서로 일치하지 않습니다.");
//        } else {
//            redirectAttributes.addFlashAttribute("message", "비밀번호 변경 성공!");
//            userDTO.setPassword(passwordEncoder.encode(newPw));
//            userService.update(userDTO);
//        }
//
////        return "redirect:/user/mypage/" + id;
//    }

}
