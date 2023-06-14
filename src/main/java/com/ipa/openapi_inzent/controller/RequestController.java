package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonObject;
import com.ipa.openapi_inzent.config.auth.UserCustomDetails;
import com.ipa.openapi_inzent.model.RequestDTO;
import com.ipa.openapi_inzent.model.UserDTO;
import com.ipa.openapi_inzent.service.RequestService;
import com.ipa.openapi_inzent.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class RequestController {
    RequestService requestService;
    UserService userService;

    @Autowired
    public RequestController(RequestService requestService, UserService userService) {
        this.userService = userService;
        this.requestService = requestService;
    }

    @GetMapping("/requestPage")
    public String Authorization(Model model) {
        List<RequestDTO> list = requestService.selectAll();

        model.addAttribute("list", list);
        return "requestPage";
    }

    @GetMapping("/selectOne/{id}")
    @ResponseBody
    public JsonObject selectOne(@PathVariable int id) {
        JsonObject object = new JsonObject();
        RequestDTO requestDTO = requestService.selectOne(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String entryDate = sdf.format(requestDTO.getEntryDate());
        String procDate = null;
        if (requestDTO.getProcDate() != null) {
            procDate = requestDTO.getProcDate();
        }

        object.addProperty("id", id);
        object.addProperty("entryDate", entryDate);
        object.addProperty("procDate", procDate);
        object.addProperty("procUsername", requestDTO.getProcUsername());
        object.addProperty("title", requestDTO.getTitle());
        object.addProperty("content", requestDTO.getContent());
        object.addProperty("status", requestDTO.getStatus());
        object.addProperty("userId", requestDTO.getUserId());
        object.addProperty("reqUsername", requestDTO.getReqUsername());
        object.addProperty("reqNickname", requestDTO.getReqNickname());

        System.out.println("object = " + object);

        return object;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        requestService.delete(id);
        return "redirect:/requestPage";
    }

    @GetMapping("/refuse/{id}")
    public String refuse(@PathVariable int id, @AuthenticationPrincipal UserCustomDetails userDetails) {
        UserDTO logIn = userDetails.getUserDTO();
        if (logIn == null) {
            return "redirect:/user/login";
        }
        System.out.println("id = " + id);
        System.out.println("logIn = " + logIn);
        RequestDTO requestDTO = requestService.selectUserId(id);
        System.out.println("requestDTO = " + requestDTO);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String procDate = sdf.format(now);

        // 계정 거절
        requestDTO.setStatus(false);
        requestDTO.setProcDate(procDate);
        requestDTO.setProcUsername(logIn.getUsername());

        requestService.updateRequest(requestDTO);
        userService.delete(id);
        return "redirect:/requestPage";
    }

    @GetMapping("/approve/{id}")
    public String approve(@PathVariable int id, @AuthenticationPrincipal UserCustomDetails userDetails) {
        UserDTO logIn = userDetails.getUserDTO();
        if (logIn == null) {
            return "redirect:/user/login";
        }
        System.out.println("id = " + id);
        RequestDTO requestDTO = requestService.selectUserId(id);
        System.out.println("requestDTO = " + requestDTO);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String procDate = sdf.format(now);

        // 계정 승인
        requestDTO.setStatus(true);
        requestDTO.setProcDate(procDate);
        requestDTO.setProcUsername(logIn.getUsername());

        // 계정 활성화
        UserDTO userDTO = userService.userOne(requestDTO.getUserId());
        userDTO.setActivate(true);
        userDTO.setApprove(true);
        userService.update(userDTO);

        requestService.updateRequest(requestDTO);
        return "redirect:/requestPage";
    }

    @GetMapping("/requestSearch")
    public String requestSearch(Model model, String keyword) {
        model.addAttribute("list", requestService.requestSearch(keyword));
        System.out.println("requestService.requestS earch(keyword) = " + requestService.requestSearch(keyword));
        return "requestPage";
    }

}
