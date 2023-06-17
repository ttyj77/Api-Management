package com.ipa.openapi_inzent.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipa.openapi_inzent.config.auth.UserCustomDetails;
import com.ipa.openapi_inzent.config.auth.UserCustomDetailsService;
import com.ipa.openapi_inzent.model.ApiDetailsDTO;
import com.ipa.openapi_inzent.model.AppUserDTO;
import com.ipa.openapi_inzent.model.UserDTO;
import com.ipa.openapi_inzent.model.UserRoleDTO;
import com.ipa.openapi_inzent.service.UserService;
import io.swagger.annotations.ResponseHeader;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class OAuthController {


    private UserCustomDetailsService userCustomDetailsService;
    private UserService userService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public OAuthController(UserCustomDetailsService userCustomDetailsService, UserService userService) {
        this.userCustomDetailsService = userCustomDetailsService;
        this.userService = userService;
    }

//    @GetMapping("/authorized")
//    public void authorized() {
//        System.out.println("OAuthController.authorized");
//    }


//    @GetMapping(value = "/getEmployees")
//    public ModelAndView getEmployeeInfo() {
//        return new ModelAndView("getEmployees");
//    }

    @GetMapping("/callback")
    public void callback() {
        System.out.println("OAuthController.callback");
    }

    @GetMapping("authorizedTest")
    @ResponseBody
    public String authorizedTest() {
        return "connection";
    }


    @GetMapping(value = "/authorized")
    public String showEmployees(@RequestParam("code") String code, HttpServletRequest httpServletRequest) throws JsonProcessingException, IOException {
        System.out.println("Authorization Code------" + code);
//
        ResponseEntity<String> response;
        RestTemplate restTemplate = new RestTemplate();
        // According OAuth documentation we need to send the client id and secret key in the header for authentication
        String credentials = "client:secret";


        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
        System.out.println("encodedCredentials = " + encodedCredentials);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Basic " + encodedCredentials);

        ClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate = new RestTemplate(httpRequestFactory);
        String access_token_url = "http://localhost:9000/oauth2/token";
        access_token_url += "?code=" + code;
        access_token_url += "&grant_type=authorization_code";
        access_token_url += "&redirect_uri=http://localhost:8080/authorized";
        access_token_url += "&code_verifier=A7MvYn9hmuJQZt7Unepbx9khicAo2IWAAhSCAbeSoA2";
        HttpEntity<String> request = new HttpEntity<String>(headers);
        response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
        System.out.println("Access Token Response ---------" + response.getBody());

        // Get the Access Token From the recieved JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response.getBody());
        String token = node.path("access_token").asText();
        System.out.println("******************************************");
        System.out.println("token = " + token);

        String url = "http://localhost:8000/resource/userInfo";

        // Use the access token for authentication
        HttpHeaders headers1 = new HttpHeaders();
        headers1.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers1);
        ResponseEntity<String> responseResource = null;
        responseResource = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        System.out.println("responseResource = " + responseResource.getBody());
//        UserDetails body = responseResource.getBody().get;
        System.out.println("responseResource = " + responseResource.getStatusCodeValue());

        // 회원가입 시키기
        //1. sub 데이터 뽑기
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(responseResource.getBody());
        JsonObject object = (JsonObject) jsonObject.get("principal");


        JsonObject result = (JsonObject) object.get("claims");

        String username = String.valueOf(result.get("sub")).replaceAll("\\\"", "");
        String nickname = String.valueOf(result.get("nickname")).replaceAll("\\\"", "");
        JsonArray roles = (JsonArray) result.get("roles");

        System.out.println("=======================================================");
        System.out.println(nickname);

        List<UserDTO> userDTO = userService.findByUsername("inzent_" + username);

        UserDTO userDTO1 = new UserDTO();
        System.out.println("userDTO = " + userDTO);

        if (userDTO.isEmpty()) { // 첫 소셜로그인
            System.out.println("처음 로그인 함");
            userDTO1.setUsername("inzent_" + username);
//            userDTO1.setNickname("ROLE_NORMAL");
            userDTO1.setPassword(passwordEncoder.encode("password"));
            userDTO1.setNickname(nickname);
            userDTO1.setToken(token);
            userDTO.add(userDTO1);
            System.out.println(userDTO);

            int id = userService.register(userDTO1);

            // 역할 넣기 (일반 사용자 2)
            UserRoleDTO userRoleDTO = new UserRoleDTO();
            userRoleDTO.setUserId(id);
            userRoleDTO.setRoleId(2); // 일반 사용자 ( ROLE_NORMAL )
            userService.insertRole(userRoleDTO);
        } else {
            System.out.println("이후 로그인 부터는 토큰만 업데이트 해주면 됨");
            UserDTO updateUser = userDTO.get(0);
            updateUser.setToken(token);
            System.out.println("updateUser = " + updateUser);
            userService.update(updateUser);
            System.out.println("업데이트 완료");
        }
        System.out.println();
        System.out.println();
        System.out.println("======================");
        System.out.println();
        System.out.println("token = " + token);
        System.out.println();
        UserCustomDetails userDetails = (UserCustomDetails) userCustomDetailsService.loadUserByUsername("inzent_" + username);
        System.out.println("===========================================");
        System.out.println("userDetails = " + userDetails.getUserDTO());
        System.out.println("userDetails.getClass() = " + userDetails.getClass());

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        // 인증 객체를 현재 스레드의 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(auth);

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("logIn", userDetails.getUserDTO());

        return "redirect:/api";
    }

    @GetMapping("/accessTokenCheck")
    @ResponseBody
    public void check(HttpSession session, @AuthenticationPrincipal UserCustomDetails userCustomDetails) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = new UserDTO();

        if (principal instanceof UserDetails) {
            //일반로그인
            String username = ((UserDetails) principal).getUsername();
            System.out.println("username 1 = " + username);
            System.out.println((UserDetails) principal);
            userDTO = userCustomDetails.getUserDTO();
        } else {
            //인젠트 로그인
            UserDTO logIn = (UserDTO) session.getAttribute("logIn");
            System.out.println("==============" + logIn);
            String username = principal.toString();
            System.out.println("username 2  = " + username);
            System.out.println("userinfo 2  " + principal);
            userDTO = logIn;
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8000/resource/check";
        String token = userService.findToken(userDTO.getId());
        System.out.println("token = " + token);

        // Use the access token for authentication
        HttpHeaders headers1 = new HttpHeaders();
        headers1.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers1);
        ResponseEntity<String> responseResource = null;
        responseResource = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

//        System.out.println("responseResource = " + responseResource.getBody());
////        UserDetails body = responseResource.getBody().get;
//        System.out.println("responseResource = " + responseResource.getStatusCodeValue());
    }

}