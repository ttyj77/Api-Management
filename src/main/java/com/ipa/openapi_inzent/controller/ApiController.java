package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.util.Map;

@RequestMapping("/api")
@Controller
public class ApiController {
    ApiService apiService;
    RoleService roleService;
    ApiDetailsService apiDetailsService;

    String[] array = {"get", "post", "put", "delete"};

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
        System.out.println("apisId = " + apisId);

        ApiDTO a = apiService.selectOne(apisId); // detail 맨 위 정보 때문에 필요 (ex. 보험업권) // apisId
        List<ResourceDTO> resourceList = apiDetailsService.resourceList(apisId); // apisId
        List<ApiDetailsDTO> apiDetailsDTOList = apiDetailsService.detailsList(apisId);
        List<TagDTO> tagList = apiDetailsService.selectAllTag();

//        System.out.println("apiDetailsDTOList = " + apiDetailsDTOList);
//
//        System.out.println("a = " + a);
//        System.out.println("resourceList = " + resourceList);

        model.addAttribute("api", a);
        model.addAttribute("resourceIndex", resourceList);
        model.addAttribute("apiDetailsDTOList", apiDetailsDTOList);
        model.addAttribute("tagList", tagList);

        return "/apis/details";
    }


    @GetMapping("/delete/{id}")
    public String apiDelete(@PathVariable int id) {
        apiService.delete(id);
        return "redirect:/api";
    }

    @PostMapping("/insert")
    public String insert(ApiDTO apiDTO, ApisRoleDTO
            apisRoleDTO, @RequestParam(value = "roleId") List<String> roleId) {
        System.out.println("id===================" + roleId);
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

    // ########################################
    //             Api Details Part
    // ########################################


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

    @PostMapping("/resource/insert")
    @ResponseBody
    public void insertResources(@RequestBody Map<String, String> paramMap) {
        System.out.println("ApiController.insertResources");
        System.out.println("\n");
        System.out.println("=======================================");
        System.out.println("[ModuleApiController] : [testPostBodyJson] : [start]");
        System.out.println("[request keySet] : " + String.valueOf(paramMap.keySet()));
        System.out.println("[request idx] : " + String.valueOf(paramMap.get("idx"))); //apis id
        System.out.println("[request uriId] : " + String.valueOf(paramMap.get("uriId"))); // resource id
        System.out.println("[request url] : " + String.valueOf(paramMap.get("url"))); // https:8080
        System.out.println("[request path] : " + String.valueOf(paramMap.get("path"))); // resource  /vi/insuf/
        System.out.println("[request get] : " + String.valueOf(paramMap.get("get")));

        JsonObject object = new JsonObject();
        object = (JsonObject) JsonParser.parseString(paramMap.get("get"));

        System.out.println(object.size()); // 0
        System.out.println(object.isEmpty()); // true

        System.out.println("[request post] : " + String.valueOf(paramMap.get("post")));
        System.out.println("[request put] : " + String.valueOf(paramMap.get("put")));
        System.out.println("[request delete] : " + String.valueOf(paramMap.get("delete")));
        System.out.println("=======================================");
        System.out.println("\n");

//        String uriId = String.valueOf(paramMap.get("uriId"));
        int apiId = Integer.parseInt(paramMap.get("uriId"));


        ResourceDTO resourceDTO = new ResourceDTO();
//        resourceDTO.setApisId(apiId);


        getTagId(paramMap, apiId);
        // 이미 있는 리소스에 메소드만 추가 한 것인지


        // tag가 있는지 없는지


//        1 ) 존재하는 리소스 인지 새로 생성해야되는 리소스 인지 판별
//            1-1) 존재한다면 중복 메소드는 막아야된다.
//        if (uriId.equals("0")) {
////            신규 리소스 생성
//            System.out.println("신규 리소스 생성");
//
////
////                // 1. 리소스 등록
//////                apiDetailsService.insertResource(resourceDTO);
////
////            }
//        } else {
////            기존 리소스에 추가 등록
//            System.out.println("기존 리소스에 추가 등록");
//
//        }
////        2) 존재하지 않는다면 리소스를 등록해야 된다.
////            2-1) resource 등록
////            2-2) apiDetails 등록
////            2-3) parameters/ body/ response 등록


    }

    private void getTagId(Map<String, String> paramMap, int apiId) {
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setApisId(apiId);
        for (int i = 0; i < array.length; i++) {

            JsonObject obj = (JsonObject) JsonParser.parseString(paramMap.get(array[i]));
            if (!obj.isEmpty()) {


                System.out.println(obj.isEmpty());


                if (obj.get("tag") == null || obj.get("tag").toString().replaceAll("[^\\w+]", "").isEmpty()) {
                    System.out.println("태그 없음.");
                } else {
                    System.out.println("태그 있음..");
                    int tagId = Integer.parseInt(obj.get("tag").toString().replaceAll("[^\\w+]", ""));
                    resourceDTO.setTagId(tagId);


                }

                int uriId = Integer.parseInt(paramMap.get("uriId"));
                System.out.println(uriId == 0);
                if (uriId == 0) { //기존의 리소스가 아니라 새로 등록이라면 apiDetails 전에 resource 먼저 등록
                    System.out.println("리소스 새로등록");
                    resourceDTO.setApisId(Integer.parseInt(paramMap.get("idx"))); // apis 아이디
                    System.out.println("resourceDTO = " + resourceDTO);

                    uriId = apiDetailsService.insertResource(resourceDTO); // 리소스 생성
                    System.out.println("uriId = " + uriId);
                }
                ///있는 리소스라면
                System.out.println("++++++++++++++++++++++++++++++");
                ApiDetailsDTO apiDetailsDTO = new ApiDetailsDTO();
                System.out.println(obj.get("operation"));
                System.out.println(obj.get("summary"));
                System.out.println(obj.get("param"));
                System.out.println(obj.get("resCode"));

                apiDetailsDTO.setOperationId(obj.get("operation").toString());
                apiDetailsDTO.setSummary(obj.get("summary").toString());
                apiDetailsDTO.setUrl(paramMap.get("url"));
                apiDetailsDTO.setUri(paramMap.get("path"));
                apiDetailsDTO.setMethod(array[i].toUpperCase());
                apiDetailsDTO.setResourceId(uriId);
                apiDetailsDTO.setApisId(apiId);
                System.out.println("apiDetailsDTO = " + apiDetailsDTO);

                apiDetailsService.insertApiDetail(apiDetailsDTO);
                System.out.println("++++++++++++++++++++++++++++++");
                System.out.println();

            }
        }
    }

    @GetMapping("/select/detail")
    @ResponseBody
    public JsonObject detailModal(int id) {
        JsonObject object = new JsonObject();
        ApiDetailsDTO detailList = apiDetailsService.searchDetail(id);

        object.addProperty("id", detailList.getId());
        object.addProperty("id", detailList.getId());
        object.addProperty("id", detailList.getId());
        object.addProperty("id", detailList.getId());
        object.addProperty("id", detailList.getId());
        object.addProperty("id", detailList.getId());
        object.addProperty("id", detailList.getId());
        object.addProperty("id", detailList.getId());
        object.addProperty("id", detailList.getId());

        return object;
    }


    @GetMapping("/search/path")
    @ResponseBody
    public JsonObject searchKeyword(String keyword) {
        List<ApiDetailsDTO> searchList = apiDetailsService.searchPath(keyword);
        JsonArray array = new JsonArray();
        for (ApiDetailsDTO a : searchList) {
            JsonObject object = new JsonObject();
            object.addProperty("id", a.getId());
            object.addProperty("uri", a.getUri());
            object.addProperty("method", a.getMethod());
            array.add(object);
        }
        JsonObject result = new JsonObject();
        result.addProperty("responseText", array.toString());

        return result;
    }

    @GetMapping("/resourceModal")
    public String resourceModal() {
        return "/apis/resourceModal";
    }


    // ########################################
    //             Api Details Part
    // ########################################

    @GetMapping("/trash")
    public String apiTrash(Model model) {
        List<ResourceDTO> rlist = apiDetailsService.goTrashResource();
        List<ApiDetailsDTO> adlist = apiDetailsService.goTrashDetail();
        List<ApiDetailsDTO> temp = new ArrayList<>();

        model.addAttribute("rlist", rlist);
        model.addAttribute("adlist", adlist);

        return "/apis/trash";
    }

    @GetMapping("/completeDelete/{id}")
    public String completeDetele(@PathVariable int id) {
        System.out.println("id = " + id);
        System.out.println("ApiController.completeDetele");
//        apiDetailsService.completeDelete(id);
        return "redirect:/api/trash";
    }

    @GetMapping("/resourceDelete/{id}")
    public String resourceDetele(@PathVariable int id) {
        System.out.println("id = " + id);
        System.out.println("ApiController.resourceDetele");
//        apiDetailsService.resourceDelete(id);
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
        a.setTrash(true);
        apiDetailsService.updateDetail(a);
        return "redirect:/api/details/" + a.getApisId();
    }

    @GetMapping("/goTrashResource/{id}")
    public String goTrashResource(@PathVariable int id) {
        ResourceDTO s = apiDetailsService.resourceOne(id);
        s.setGarbage(true);
        apiDetailsService.updateResource(s);
        return "redirect:/api/details/" + s.getApisId();
    }
}