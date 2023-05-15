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

        return "/apis/index";
    }

    @GetMapping("/selectOne")
    @ResponseBody
    public JsonObject apiOne(int apiId) {
        JsonObject object = new JsonObject();
        ApiDTO apiDTO = apiService.selectOne(apiId);
        List<RoleDTO> apiSelectedRole = roleService.selectApisRoleList(apiId);

        object.addProperty("apiId", apiId);
        object.addProperty("apiName", apiDTO.getName());
        object.addProperty("apiContext", apiDTO.getContext());
        object.addProperty("apiExplanation", apiDTO.getExplanation());
        object.addProperty("apiDisclosure", apiDTO.isDisclosure());

        JsonArray selectRoleArray = new JsonArray();
        for (RoleDTO role : apiSelectedRole) {
            JsonObject r = new JsonObject();
            r.addProperty("id", role.getId());
            r.addProperty("code", role.getCode());
            r.addProperty("name", role.getName());
            selectRoleArray.add(r);
        }
        object.addProperty("selectedRoleList", selectRoleArray.toString());

//        System.out.println("roleAll = " + roleAll);


        return object;
    }

    //단순 모든 role 출력
    @GetMapping("/roleList")
    @ResponseBody
    public JsonObject roleList() {

        JsonObject result = new JsonObject();
//        List<RoleDTO> roleList = roleService.selectApisRoleList(apiId);
        List<RoleDTO> roleAll = roleService.selectAll();

        System.out.println("roleAll = " + roleAll);
//
//        for (RoleDTO role : roleList) {
//            object.addProperty(role.getCode(), role.getName());
//        }
        JsonArray array = new JsonArray();
        for (RoleDTO r : roleAll) {
            JsonObject roleObject = new JsonObject();
            roleObject.addProperty("id", r.getId());
            roleObject.addProperty("code", r.getCode());
            roleObject.addProperty("name", r.getName());
            array.add(roleObject);
        }
        result.addProperty("responseText", array.toString());
        return result;
    }


    //api별  role 출력
    @GetMapping("/apiRoleList")
    @ResponseBody
    public JsonObject apiRoleList(int apiId) {

        JsonObject result = new JsonObject();
        List<RoleDTO> roleAll = roleService.selectAll();

//        System.out.println("roleAll = " + roleAll);
        JsonArray array = new JsonArray();
        for (RoleDTO r : roleAll) {
            JsonObject roleObject = new JsonObject();
            roleObject.addProperty("id", r.getId());
            roleObject.addProperty("code", r.getCode());
            roleObject.addProperty("name", r.getName());
            array.add(roleObject);
        }
        result.addProperty("responseText", array.toString());
        return result;
    }


    @GetMapping("/details/{apisId}") // id = apisId
    public String details(Model model, @PathVariable int apisId, HttpServletResponse response) {
        // 리소스 list, 안에 들어갈 apiId 조건으로 묶여 있는 apiDetails List 필요

        ApiDTO a = apiService.selectOne(apisId); // detail 맨 위 정보 때문에 필요 (ex. 보험업권) // apisId
        List<ResourceDTO> resourceList = apiDetailsService.resourceList(apisId); // apisId
        List<ApiDetailsDTO> apiDetailsDTOList = apiDetailsService.detailsList(apisId);

        model.addAttribute("api", a);
        model.addAttribute("resourceIndex", resourceList);
        model.addAttribute("apiDetailsDTOList", apiDetailsDTOList);

        return "/apis/details";
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

    @GetMapping("/trash")
    public String apiTrash(Model model) {
        List<ResourceDTO> rlist = apiDetailsService.goTrashResource();
        List<ApiDetailsDTO> adlist = apiDetailsService.goTrashDetail();
        List<ApiDetailsDTO> temp = new ArrayList<>();
//        for (int i = 0; i < rlist.size(); i++) {
//            for (int j = 0; j < adlist.size(); j++) {
//                if (rlist.get(i).getId() == adlist.get(j).getResourceId()) {
//
//                }
//            }
//        }

        model.addAttribute("rlist", rlist);
        model.addAttribute("adlist", adlist);

        return "/apis/trash";
    }

    @GetMapping("/completeDelete/{id}")
    public String completeDetele(@PathVariable int id) {
        apiDetailsService.completeDelete(id);

        return "redirect:/api/trash";
    }

    @GetMapping("/return/{id}")
    public String goReturn(@PathVariable int id) {
        ApiDetailsDTO a = apiDetailsService.selectOne(id);
        a.setTrash(false);
        return "redirect:/api/trash";
    }
    // 휴지통 관리로 보내는 곳
    @GetMapping("/goTrash/{id}")
    public String goTrash(@PathVariable int id) {
        ApiDetailsDTO a = apiDetailsService.selectOne(id);
        System.out.println("a = " + a);
        a.setTrash(true);
        apiDetailsService.updateDetail(a);
        System.out.println("a = " + a);
        return "redirect:/api/details/"+a.getApisId();
    }
    @GetMapping("/goTrashResource/{id}")
    public String goTrashResource(@PathVariable int id) {
        ApiDetailsDTO a = apiDetailsService.selectOne(id);
        a.setGarbage(true);
        apiDetailsService.updateResource(a);
        return "redirect:/api/details/"+a.getApisId();
    }
}
