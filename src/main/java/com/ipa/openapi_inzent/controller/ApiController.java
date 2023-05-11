package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ipa.openapi_inzent.model.*;
import com.ipa.openapi_inzent.service.ApiDetailsService;
import com.ipa.openapi_inzent.service.ApiService;
import com.ipa.openapi_inzent.service.RoleService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@Controller
public class ApiController {
    ApiService apiService;
    RoleService roleService;
    ApiDetailsService apiDetailsService;

    @Autowired
    public ApiController(ApiService apiService, ApiDetailsService apiDetailsService, RoleService roleService) {
        this.apiService = apiService;
        this.apiDetailsService = apiDetailsService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String apis(Model model) {
        model.addAttribute("list", apiService.selectAll());
        model.addAttribute("roles", roleService.selectAll());

        return "/apis/index2";
    }

    @GetMapping("/selectOne")
    @ResponseBody
    public JsonObject apiOne(int apiId) {
        JsonObject object = new JsonObject();
        ApiDTO apiDTO = apiService.selectOne(apiId);

        object.addProperty("apiId", apiId);
        object.addProperty("apiName", apiDTO.getName());
        object.addProperty("apiContext", apiDTO.getContext());
        object.addProperty("apiExplanation", apiDTO.getExplanation());
        object.addProperty("apiDisclosure", apiDTO.isDisclosure());

        return object;
    }

    @GetMapping("/roleList")
    @ResponseBody
    public JsonObject roleList(int apiId) {
        JsonObject object = new JsonObject();
//        List<ApiDTO> list = apiService.selectRoleList(apiId);
        List<RoleDTO> roleList = roleService.selectApisRoleList(apiId);
//        System.out.println("roleList = " + roleList); //해당하는 apis의  role 리스트
//        System.out.println("roleList.get(1).getName() = " + roleList.get(1).getName());

        List<RoleDTO> roleAll = roleService.selectAll();

        for (RoleDTO role : roleList) {
            object.addProperty(role.getCode(), role.getName());
        }
        JsonArray array = new JsonArray();
        for (RoleDTO r : roleAll) {
            JsonObject roleObject = new JsonObject();
            roleObject.addProperty("code", r.getCode());
            array.add(roleObject);
        }

        object.addProperty("roleAll", array.toString());
//        System.out.println("object = " + object);


        return object;
    }

    @GetMapping("/delete/{id}")
    public String apiDelete(@PathVariable int id) {
        apiService.delete(id);
        return "redirect:/api";
    }

    @PostMapping("/insert")
    public String insert(ApiDTO apiDTO, ApisRoleDTO apisRoleDTO, @RequestParam(value = "roleId") List<String> roleId) {
        int id = apiService.insertApi(apiDTO);
//        List<RoleDTO> list = roleService.selectAll();
        System.out.println("roleId = " + roleId);
        apisRoleDTO.setApisId(id);

        if (apisRoleDTO.getApisId() == 0 || apisRoleDTO.getRoleId() == 0) {
            System.out.println("fk 제약조건 위배");
        } else {
            for (String role : roleId) {
                apisRoleDTO.setRoleId(Integer.parseInt(role));
                apiService.insertRole(apisRoleDTO);
            }
            System.out.println("apisRoleDTO = " + apisRoleDTO);

        }
        return "redirect:/api";
    }

    @PostMapping("/update/{id}")
    public String update(ApiDTO apiDTO, @RequestParam(value = "roleId") List<String> roleId, @PathVariable int id) {
        apiService.update(apiDTO);
        System.out.println("roleId = " + roleId);
        System.out.println("id = " + id);
        List<ApiDTO> roleList = apiService.selectRoleList(id);
        ApisRoleDTO apisRoleDTO = new ApisRoleDTO();
        apisRoleDTO.setApisId(id);
        // 수정시 업데이트될 역할들어갈 list
        List<Integer> roles = new ArrayList<>();

        if (apisRoleDTO.getApisId() == 0 || apisRoleDTO.getRoleId() == 0) {
            System.out.println("선택된 ROLE 없음");
        } else {
            for (ApiDTO roleOne : roleList) {
                for (String role : roleId) {
                    if (roleOne.getRoleId() == Integer.parseInt(role)) {
                        System.out.println("");
                    }
                    apisRoleDTO.setRoleId(Integer.parseInt(role));
                    apiService.updateRole(apisRoleDTO);
                }
            }
        }
        return "redirect:/api";
    }

    @GetMapping("/trash")
    public String apiTrash() {

        return "/apis/trash";
    }

    @GetMapping("/resourceModal")
    public String resourceModal() {
        return "/apis/resourceModal";
    }


    // ########################################
    //             Api Details Part
    // ########################################

    @GetMapping("/details/{apisId}") // id = apisId
    public String details(Model model, @PathVariable int apisId, HttpServletResponse response) {
        // 리소스 list, 안에 들어갈 apiId 조건으로 묶여 있는 apiDetails List 필요

        ApiDTO a = apiService.selectOne(apisId); // detail 맨 위 정보 때문에 필요 (ex. 보험업권) // apisId
        List<ResourceDTO> resourceList = apiDetailsService.resourceList(apisId); // apisId
        List<ApiDetailsDTO> apiDetailsDTOList = apiDetailsService.detailsList(apisId);
//        System.out.println("apiDetailsDTOList = " + apiDetailsDTOList);
//
//        System.out.println("a = " + a);
//        System.out.println("resourceList = " + resourceList);

        model.addAttribute("api", a);
        model.addAttribute("resourceIndex", resourceList);
        model.addAttribute("apiDetailsDTOList", apiDetailsDTOList);

        return "/apis/details";
    }

    @PostMapping("/resource/{id}")
    @ResponseBody
    public JsonObject resources(Model model, @PathVariable int id) {
        // 리소스 하나씩
        List<ApiDetailsDTO> resourceInAdList = apiDetailsService.resourceInAdList(id); // resource table id
        System.out.println("resourceInAdList = " + resourceInAdList);

        JsonObject object = new JsonObject();
        List<ApiDetailsDTO> adList = apiDetailsService.resourceInAdList(id);


        return object;
    }
}
