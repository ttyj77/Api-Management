package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipa.openapi_inzent.config.auth.UserCustomDetails;
import com.ipa.openapi_inzent.model.*;
import com.ipa.openapi_inzent.service.ApiDetailsService;
import com.ipa.openapi_inzent.service.ApiService;
import com.ipa.openapi_inzent.service.RoleService;
import com.ipa.openapi_inzent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/api")
@Controller
public class ApiController {

    ApiService apiService;
    RoleService roleService;
    ApiDetailsService apiDetailsService;
    UserService userService;

    String[] array = {"get", "post", "put", "delete"};

    @Autowired
    public ApiController(ApiService apiService, UserService userService, ApiDetailsService apiDetailsService, RoleService roleService) {
        this.apiService = apiService;
        this.apiDetailsService = apiDetailsService;
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping("")
    public String apis(Model model, HttpSession session, @AuthenticationPrincipal UserCustomDetails userCustomDetails) {

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

        // 순수 apis 목록
        List<ApiDTO> apisList = apiService.selectAll();
        // apisRole + role 목록
        List<RoleDTO> apisRoleList = roleService.apisRoles();
        // 로그인 한 user role과 apis role을 비교하기 위해 userRole + userDTO 목록 ( 한 유저의 Role 들)
        List<UserRoleDTO> userRoleList = userService.roleName(userDTO.getId());
        // 비공개 애들 넣을 빈 리스트
        List<Integer> noShow = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        // 비공개 애들의 role들을 넣을 리스트
        List<RoleDTO> noShowRoles = new ArrayList<>();
        // apis 페이지에 보낼 최종 APIs 목록
        List<ApiDTO> passList = new ArrayList<>();

        // 비공개 + role 지정 없는 애들 넣을 목록
        List<Integer> nothing = new ArrayList<>();

        // APIs들 최종 리스트에 추가
        for (ApiDTO a : apisList) {
            if (a.isDisclosure()) {
                // 공개인 APIs
                passList.add(a);
            } else if (!a.isDisclosure()) {
                // 비공개인 APIs
                noShow.add(a.getId());
                temp.add(a.getId());
            }
        }

        for (RoleDTO r : apisRoleList) {
            for (int id : noShow) {
                if (r.getApisId() == id) {
                    RoleDTO roleDTO = new RoleDTO();
                    roleDTO.setRoleId(r.getRoleId());
                    roleDTO.setCode(r.getCode());
                    roleDTO.setApisId(r.getApisId());
                    noShowRoles.add(roleDTO);
                } else if (r.getApisId() != id) {
//                    nothing.add(r.getApisId());
                }
            }
        }

        // 비공개이면서 지정된 role이 없는 APIs => 관리자만 볼 수 있음
        for (RoleDTO r : apisRoleList) {
            for (int id : noShow) {
                if (r.getApisId() == id) {
                    temp.remove(Integer.valueOf(id));
                }
            }
        }

        // 비공개 + 지정된 role이 없는 APIs들 관리자 권한만 부여, passList에 추가
        for (UserRoleDTO userRoleDTO : userRoleList) {
            for (int apisId : temp) {
                if (userRoleDTO.getCode().equals("ADMIN")) {
                    ApiDTO apiDTO = apiService.selectOne(apisId);
                    passList.add(apiDTO);
                }
            }
        }
        // 역할에 맞게 APIs들 보임
        for (UserRoleDTO userRoleDTO : userRoleList) {
            for (RoleDTO roleDTO : noShowRoles) {
                if (userRoleDTO.getRoleId() == roleDTO.getRoleId()) {
                    ApiDTO apiDTO = apiService.selectOne(roleDTO.getApisId());
                    passList.add(apiDTO);
                }
            }
        }

        List<ApiDTO> newList = passList.stream().distinct().collect(Collectors.toList());
        model.addAttribute("list", newList);
        return "/apis/index";
    }

    @GetMapping("/grant")
    @ResponseBody
    public JsonObject giveRole() {
        JsonObject object = new JsonObject();
        List<ApiDTO> list = apiService.giveRole();
        // 역할 없는 비공개인 apis들을 분류하기 위해 apis list 호출
        List<ApiDTO> apiDTOList = apiService.selectAll();

        JsonArray jsonArray = new JsonArray();
        for (ApiDTO a : apiDTOList) {
            JsonObject object1 = new JsonObject();
            // 모든 비공개인 것들
            if (!a.isDisclosure()) {
                object1.addProperty("apisId", a.getId());
                jsonArray.add(object1);
            }
        }

        object.addProperty("apisList", jsonArray.toString());

        JsonArray array1 = new JsonArray();
        for (ApiDTO apiDTO : list) {
            JsonObject jsonObject = new JsonObject();
            // 역할 있는 비공개인 것들
            if (!apiDTO.isDisclosure()) {
                jsonObject.addProperty("apisId", apiDTO.getApisId());
                jsonObject.addProperty("roleId", apiDTO.getRoleId());
                jsonObject.addProperty("code", apiDTO.getCode());
                array1.add(jsonObject);
            }
        }
        object.addProperty("list", array1.toString());

        return object;
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

        return object;
    }

    //단순 모든 role 출력
    @GetMapping("/roleList")
    @ResponseBody
    public JsonObject roleList() {

        JsonObject result = new JsonObject();
        List<RoleDTO> roleAll = roleService.selectAll();

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
    public JsonObject apiRoleList() {

        JsonObject result = new JsonObject();
        List<RoleDTO> roleAll = roleService.selectAll();

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
        List<TagDTO> tagList = apiDetailsService.selectAllTag();

        model.addAttribute("api", a);
        model.addAttribute("resourceIndex", resourceList);
        for (ApiDetailsDTO api : apiDetailsDTOList) {
            api.setSummary(api.getSummary().replaceAll("\"", ""));
            api.setOperationId(api.getOperationId().replaceAll("\"", ""));
        }
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
        int id = apiService.insertApi(apiDTO);
        apisRoleDTO.setApisId(id);
        if (roleId.contains("0")) {
            roleId.remove(0);
        }

        if (roleId.isEmpty()) {
            System.out.println("role 선택 안됨");
        } else {
            for (String role : roleId) {
                apisRoleDTO.setRoleId(Integer.parseInt(role));
                apiService.insertRole(apisRoleDTO);
            }
        }
        return "redirect:/api";
    }

    @PostMapping("/update/{id}")
    public String update(String context, String name, boolean disclosure, String explanation, @RequestParam(value = "roleId") List<String> roleId, @PathVariable int id) {
        ApiDTO apiDTO = new ApiDTO();
        apiDTO.setId(id);
        apiDTO.setContext(context);
        apiDTO.setName(name);
        apiDTO.setDisclosure(disclosure);
        apiDTO.setExplanation(explanation);
        apiService.update(apiDTO);
        List<ApiDTO> roleList = apiService.selectRoleList(id);
        ApisRoleDTO apisRoleDTO = new ApisRoleDTO();
        apisRoleDTO.setApisId(id);
        apiService.deleteRole(id);
        if (roleId.contains("0")) {
            roleId.remove(0);
        }

        if (roleId.isEmpty()) {
            System.out.println("선택된 ROLE 없음");
        } else {
            for (String role : roleId) {
                apisRoleDTO.setRoleId(Integer.parseInt(role));
                apiService.insertRole(apisRoleDTO);
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
        System.out.println("[request uriId] : " + String.valueOf(paramMap.get("uriId"))); // resource id
        System.out.println("[request url] : " + String.valueOf(paramMap.get("url"))); // https:8080
        System.out.println("[request path] : " + String.valueOf(paramMap.get("path"))); // resource  /vi/insuf/
        System.out.println("[request get] : " + String.valueOf(paramMap.get("get")));

        JsonObject object = new JsonObject();
        object = (JsonObject) JsonParser.parseString(paramMap.get("get"));

        System.out.println("[request post] : " + String.valueOf(paramMap.get("post")));
        System.out.println("[request put] : " + String.valueOf(paramMap.get("put")));
        System.out.println("[request delete] : " + String.valueOf(paramMap.get("delete")));
        System.out.println("=======================================");
        System.out.println("\n");

        System.out.println(paramMap);
        int resourceId = 0;
        if (!(paramMap.get("resourceId")).equals("")) {
            resourceId = Integer.parseInt(paramMap.get("resourceId")); // 리소스 아이디
        }
        int apiId = Integer.parseInt(paramMap.get("idx")); // APIS 아이디

        getTagId(paramMap, apiId, resourceId);

    }

    private void getTagId(Map<String, String> paramMap, int apiId, int resourceId) {
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setApisId(resourceId);
        int uriId = Integer.parseInt(paramMap.get("uriId"));
        String resId = String.valueOf(paramMap.get("resourceId"));

        for (int i = 0; i < array.length; i++) {

            JsonObject obj = (JsonObject) JsonParser.parseString(paramMap.get(array[i]));
            if (!obj.isEmpty()) {
                System.out.println(obj.isEmpty());
                if (obj.get("tag") == null || obj.get("tag").toString().replaceAll("[^\\w+]", "").isEmpty()) {
                    System.out.println("태그 없음.");
                } else {
                    System.out.println("태그 있음.");
                    int tagId = Integer.parseInt(obj.get("tag").toString().replaceAll("[^\\w+]", ""));
                    resourceDTO.setTagId(tagId);
                }
                // uri 아이디가 넘어오지 않는다면 리소스가 새롭게 등록되어야 되지만
                if (resourceId == 0) { //기존의 리소스가 아니라 새로 등록이라면 apiDetails 전에 resource 먼저 등록
                    System.out.println("리소스 새로등록");
                    resourceDTO.setApisId(Integer.parseInt(paramMap.get("idx"))); // apis 아이디
                    System.out.println("resourceDTO = " + resourceDTO);

                    resourceId = apiDetailsService.insertResource(resourceDTO); // 리소스 생성
                } else { ///있는 리소스라면 받아온 리소스 아이디 입력
                    System.out.println("기존 리소스 사용");
                }
                ApiDetailsDTO apiDetailsDTO = new ApiDetailsDTO();

                apiDetailsDTO.setOperationId(obj.get("operation").toString());
                apiDetailsDTO.setSummary(obj.get("summary").toString());
                apiDetailsDTO.setUrl(paramMap.get("url"));
                apiDetailsDTO.setUri(paramMap.get("path"));
                apiDetailsDTO.setMethod(array[i].toUpperCase());
                apiDetailsDTO.setResourceId(resourceId);
                apiDetailsDTO.setApisId(apiId);


                int apiDetailsId = apiDetailsService.insertApiDetail(apiDetailsDTO);  // 페이지 아이디?
                System.out.println("//////////////////param/////////////////////");

                JsonArray paramArr = (JsonArray) obj.get("param");
                // 파라미터가 있다면 등록
                if (paramArr.size() != 0) {
                    for (int k = 0; k < paramArr.size(); k++) {
                        JsonObject param = (JsonObject) paramArr.get(k);

                        ParameterDTO parameterDTO = new ParameterDTO();
                        parameterDTO.setApiDetailsId(apiDetailsId);
                        parameterDTO.setName(String.valueOf(param.get("name")));
                        parameterDTO.setTransferMethod(String.valueOf(param.get("transferMethod")));
                        parameterDTO.setExplanation(String.valueOf(param.get("explanation")));
                        Boolean b = Boolean.valueOf(String.valueOf(param.get("required")));
                        parameterDTO.setRequired(b);
                        parameterDTO.setType(String.valueOf(param.get("type")));
                        parameterDTO.setSample(String.valueOf(param.get("sample")));

                        apiDetailsService.insertParameter(parameterDTO);
                    }
                } else {
                    System.out.println("parma size == 0");
                }

                System.out.println("//////////////////resCode/////////////////////");
                JsonArray resParamArr = (JsonArray) obj.get("resCode");
                if (resParamArr.size() > 0) {
                    System.out.println("응답코드 등록");

                    for (int k = 0; k < resParamArr.size(); k++) {
                        JsonObject resParam = (JsonObject) resParamArr.get(k);
                        String[] paramKeyList = String.valueOf(resParam.get("paramKey")).split(",");
                        String[] paramValueList = String.valueOf(resParam.get("paramValue")).split(",");
                        String[] paramTypeList = String.valueOf(resParam.get("paramType")).split(",");

//                        2. 분리 후 FOR문을 통해 DB에 넣는다.
//                2-1. response 에 넣기 위해서는 apiDetails id 필요
                        ResponseDTO responseDTO = new ResponseDTO();
                        responseDTO.setApiDetailsId(apiDetailsId);
                        responseDTO.setRespCode(String.valueOf(resParam.get("code")));
                        responseDTO.setRespMsg(String.valueOf(resParam.get("explanation")));
                        responseDTO.setType(String.valueOf(resParam.get("type")));
                        int responsId = apiDetailsService.insertResponse(responseDTO);
//                2-2. 키와 VALUE 값을 콤마를 기준으로 분리한다.
                        for (int j = 0; j < paramKeyList.length; j++) {
                            ResParamDTO resParamDTO = new ResParamDTO();
                            resParamDTO.setResId(responsId);
                            resParamDTO.setKey(paramKeyList[j]);
                            resParamDTO.setValue(paramValueList[j]);
                            resParamDTO.setType(paramTypeList[j]);

                            // 응답 파라미터 등록
                            apiDetailsService.insertResParam(resParamDTO);
                        }
                    }
                }
//                2-3. resParam 넣으려면 response table id값 필요 JSON으로 바꿔서 넣기
                System.out.println("//////////////////reqData/////////////////////");

                if (String.valueOf(obj.get("reqData")).replaceAll("\\\\", "") == null) {
                    System.out.println("데이터 없음");
                } else if (String.valueOf(obj.get("reqData")) == "null") {
                    System.out.println("데이터 없음2");
                } else {
                    System.out.println("res 존재");

                    String req = String.valueOf(obj.get("reqData")).replaceAll("\\\\", "");
                    req = req.substring(1);
                    req = req.substring(0, req.length() - 1);
                    try {
                        if (!JsonParser.parseString(req).isJsonNull()) {
                            JsonObject jsonObject = (JsonObject) JsonParser.parseString(req);

                            /* key를 뽑아서 리스트로 변환*/
                            List<String> keys = new ArrayList<>(jsonObject.keySet());

                            for (int j = 0; j < jsonObject.keySet().size(); j++) {
                                BodyDTO bodyDTO = new BodyDTO();
                                bodyDTO.setApiDetailsId(apiDetailsId);
                                bodyDTO.setKey(keys.get(j));
                                bodyDTO.setValue(jsonObject.get(keys.get(j)).toString().replaceAll("\"", ""));
                                apiDetailsService.insertBody(bodyDTO);
                            }
                        }
                    } catch (Exception e) {

                    }

                }
            }
        }
    }

    @GetMapping("/select/detail")
    @ResponseBody
    public JsonObject detailModal(int id) {
        JsonObject object = new JsonObject();
        ApiDetailsDTO detailList = apiDetailsService.searchDetail(id);


        object.addProperty("id", detailList.getId());
        object.addProperty("resourceId", detailList.getResourceId());
        object.addProperty("method", detailList.getMethod());
        object.addProperty("url", detailList.getUrl());
        object.addProperty("uri", detailList.getUri());
        object.addProperty("summary", detailList.getSummary());
        object.addProperty("security", detailList.getSecurity());
        object.addProperty("scope", detailList.getScope());
        object.addProperty("version", detailList.getVersion());
        object.addProperty("operationId", detailList.getOperationId());

        object.addProperty("status", detailList.getStatus());
        object.addProperty("authorization", detailList.getAuthorization());
        object.addProperty("resource_id", detailList.getResourceId());
        object.addProperty("apisId", detailList.getApisId());

//        parameter jsonArray
        JsonArray paramArray = new JsonArray();
        List<ParameterDTO> list = apiDetailsService.searchParameter(id);
        for (int i = 0; i < list.size(); i++) {
            JsonObject param = new JsonObject();
            param.addProperty("id", list.get(i).getId());
            param.addProperty("apiDetailsId", list.get(i).getApiDetailsId());
            param.addProperty("name", list.get(i).getName());
            param.addProperty("transferMethod", list.get(i).getTransferMethod());
            param.addProperty("explanation", list.get(i).getExplanation());
            param.addProperty("type", list.get(i).getType());
            param.addProperty("required", list.get(i).getRequired());
            param.addProperty("sample", list.get(i).getSample());
            paramArray.add(param);
        }


        //        parameter jsonArray
        JsonArray bodyArray = new JsonArray();
        List<BodyDTO> bodyList = apiDetailsService.selectBody(id);

        for (int i = 0; i < bodyList.size(); i++) {
            JsonObject param = new JsonObject();
            param.addProperty("id", bodyList.get(i).getId());
            param.addProperty("apiDetailsId", bodyList.get(i).getApiDetailsId());
            param.addProperty("key", bodyList.get(i).getKey());
            param.addProperty("value", bodyList.get(i).getValue());
            bodyArray.add(param);
        }


//       resCode JsonArray
        JsonArray resCodeArray = new JsonArray();

        object.addProperty("parameterList", paramArray.toString());
        object.addProperty("bodyReqList", bodyArray.toString());

        return object;
    }


    @GetMapping("/search/path")
    @ResponseBody
    public JsonObject searchKeyword(String keyword, int apisId, String defaultUri) {
        List<ApiDetailsDTO> searchList = apiDetailsService.searchPath(keyword, apisId, defaultUri);
        /*exactMatchUri 이 값이 있다면 키워드와 정확하게 일치하는 uri가 있다는 것 */
        List<ApiDetailsDTO> exactMatchUri = apiDetailsService.exactMatchUri(keyword, apisId, defaultUri);
        JsonArray array = new JsonArray();
        for (ApiDetailsDTO a : searchList) {
            JsonObject object = new JsonObject();
            object.addProperty("id", a.getId());
            object.addProperty("uri", a.getUri());
            object.addProperty("method", a.getMethod());
            object.addProperty("resourceId", a.getResourceId());
            array.add(object);
        }

        JsonArray ecactList = new JsonArray();
        for (ApiDetailsDTO a : exactMatchUri) {
            JsonObject object = new JsonObject();
            object.addProperty("id", a.getId());
            object.addProperty("uri", a.getUri());
            object.addProperty("method", a.getMethod());
            object.addProperty("resourceId", a.getResourceId());
            ecactList.add(object);
        }


        JsonObject result = new JsonObject();
        result.addProperty("responseText", array.toString());
        result.addProperty("exactMatchUri", ecactList.toString());

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
        List<ResourceDTO> resourceTrashList = apiDetailsService.resourceTrashList();

        // 리스트 null check
        List<ResourceDTO> resourceNull = apiDetailsService.resourceNull();

        // details null check
        List<ApiDetailsDTO> detailsNull = apiDetailsService.detailsNull();


        List<ApiDetailsDTO> adlist = apiDetailsService.goTrashDetail();
        List<ApiDetailsDTO> temp = new ArrayList<>();

        model.addAttribute("rlist", rlist);
        model.addAttribute("resourceTrashList", resourceTrashList);
        model.addAttribute("resourceCheck", resourceNull);
        model.addAttribute("detailsCheck", detailsNull);
        model.addAttribute("adlist", adlist);

        return "/apis/trash";
    }

    @GetMapping("/completeDelete/{id}")
    public String completeDetele(@PathVariable int id) {
        apiDetailsService.completeDelete(id);
        return "redirect:/api/trash";
    }

    @GetMapping("/resourceDelete/{id}")
    public String resourceDetele(@PathVariable int id) {
        apiDetailsService.resourceDelete(id);
        return "redirect:/api/trash";
    }

    @GetMapping("/return/{id}")
    public String goReturn(@PathVariable int id) {
        ApiDetailsDTO a = apiDetailsService.selectOne(id);
        a.setTrash(false);
        apiDetailsService.updateDetail(a);
        return "redirect:/api/trash";
    }

    @GetMapping("/returnResource/{id}")
    public String goReturnResource(@PathVariable int id) {
        ResourceDTO s = apiDetailsService.resourceOne(id);
        s.setGarbage(false);
        apiDetailsService.updateResource(s);
        return "redirect:/api/details/" + s.getApisId();
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

    @GetMapping("/trashSearch")
    public String trashSearch(Model model, String keyword) {
        model.addAttribute("list", apiDetailsService.trashSearch(keyword));
        return "/apis/trash";
    }

    @GetMapping("/resCode/selectOne")
    @ResponseBody
    public JsonObject selectRescode(int id) {
        JsonObject object = new JsonObject();
        JsonArray resArr = new JsonArray();
        JsonArray resParamArr = new JsonArray();

        List<ResponseDTO> responseList = apiDetailsService.selectResponseList(id); // details 아이디
        System.out.println("responseList = " + responseList);


        if (responseList.size() > 0) {
            System.out.println("응답코드 존재");

            for (ResponseDTO res : responseList) {
                JsonObject response = new JsonObject();
                response.addProperty("id", res.getId());
                response.addProperty("apiDetailsId", res.getApiDetailsId());
                response.addProperty("respCode", res.getRespCode());
                response.addProperty("respMsg", res.getRespMsg());
                response.addProperty("type", res.getType());
                resArr.add(response);

                int resId = res.getId(); // responseID
                List<ResParamDTO> resParamList = apiDetailsService.selectResParamList(resId); // response id
                for (ResParamDTO param : resParamList) {
                    JsonObject resParam = new JsonObject();
                    resParam.addProperty("id", param.getId());
                    resParam.addProperty("resId", param.getResId());
                    resParam.addProperty("key", param.getKey());
                    resParam.addProperty("value", param.getValue());
                    resParam.addProperty("type", param.getType());
                    resParam.addProperty("sample", param.getSample());
                    resParamArr.add(resParam);
                }
            }
        }
        /*여기서 req body 값 넣어서 보냄*/

        object.addProperty("responseList", resArr.toString());
        object.addProperty("resParamList", resParamArr.toString());


        return object;
    }


    @GetMapping("/remove/resCode")
    @ResponseBody
    public void removeResCode(int id) {
        apiDetailsService.removeResCode(id);
    }

    /*응답 파라미터 삭제*/
    @GetMapping("/remove/resParam")
    @ResponseBody
    public void removeResParam(int id) {
        apiDetailsService.removeResParam(id);
    }

    /*parameter 삭제 (파라미터 그 자체) */
    @GetMapping("/remove/param")
    @ResponseBody
    public void removeparam(int id) {
        apiDetailsService.removeParam(id);
    }

    @GetMapping("/delete/resBody")
    @ResponseBody
    public void removeResBody(int id) {
        System.out.println("ApiController.removeResBody");
        apiDetailsService.removeResBody(id);
    }

    @GetMapping("/delete/allResParamDelete")
    @ResponseBody
    public void allResParamDelete(int id) {
        System.out.println("ApiController.allResParamDelete");
        apiDetailsService.allResParamDelete(id);
    }
}

