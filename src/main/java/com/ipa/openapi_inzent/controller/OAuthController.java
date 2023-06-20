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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
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
    public String showEmployees(@RequestParam("code") String code, HttpServletRequest httpServletRequest, HttpServletRequest requestInf) throws JsonProcessingException, IOException {
        System.out.println("Authorization Code------" + code);

        ResponseEntity<String> response;
        RestTemplate restTemplate = new RestTemplate();
        // According OAuth documentation we need to send the client id and secret key in the header for authentication
        String credentials = "client:secret";


        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
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

        String url = "http://localhost:8000/resource/userInfo";

        // Use the access token for authentication
        HttpHeaders headers1 = new HttpHeaders();
        headers1.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers1);
        ResponseEntity<String> responseResource = null;
        responseResource = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // 회원가입 시키기
        //1. sub 데이터 뽑기
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(responseResource.getBody());
        JsonObject object = (JsonObject) jsonObject.get("principal");


        JsonObject result = (JsonObject) object.get("claims");

        String username = String.valueOf(result.get("sub")).replaceAll("\\\"", "");
        String nickname = String.valueOf(result.get("nickname")).replaceAll("\\\"", "");
        JsonArray roles = (JsonArray) result.get("roles");

        List<UserDTO> userDTO = userService.findByUsername("inzent_" + username);

        UserDTO userDTO1 = new UserDTO();

        if (userDTO.isEmpty()) { // 첫 소셜로그인
            System.out.println("처음 로그인 함");
            userDTO1.setUsername("inzent_" + username);
//            userDTO1.setNickname("ROLE_NORMAL");
            userDTO1.setPassword(passwordEncoder.encode("password"));
            userDTO1.setNickname(nickname);
            userDTO1.setToken(token);
            userDTO.add(userDTO1);

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
            userService.update(updateUser);
        }
        UserCustomDetails userDetails = (UserCustomDetails) userCustomDetailsService.loadUserByUsername("inzent_" + username);

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        // 인증 객체를 현재 스레드의 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(auth);

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("logIn", userDetails.getUserDTO());
//
//        if () { // app에서 들어온 요청이라면
//
//        }
        return "redirect:/api";
    }

    @GetMapping(value = "/app/authorized")
    public String appAuthorized(@RequestParam("code") String code, HttpServletRequest httpServletRequest, HttpServletRequest requestInf) throws JsonProcessingException, IOException {
        System.out.println("Authorization Code------" + code);

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
        access_token_url += "&redirect_uri=http://localhost:8080/app/authorized";
        access_token_url += "&code_verifier=A7MvYn9hmuJQZt7Unepbx9khicAo2IWAAhSCAbeSoA2";
        HttpEntity<String> request = new HttpEntity<String>(headers);
        response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
        System.out.println("Access Token Response ---------" + response.getBody());

        // Get the Access Token From the recieved JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response.getBody());
        String token = node.path("access_token").asText();

        String url = "http://localhost:8000/resource/userInfo";

        // Use the access token for authentication
        HttpHeaders headers1 = new HttpHeaders();
        headers1.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers1);
        ResponseEntity<String> responseResource = null;
        responseResource = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // 회원가입 시키기
        //1. sub 데이터 뽑기
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(responseResource.getBody());
        JsonObject object = (JsonObject) jsonObject.get("principal");


        JsonObject result = (JsonObject) object.get("claims");

        String username = String.valueOf(result.get("sub")).replaceAll("\\\"", "");
        String nickname = String.valueOf(result.get("nickname")).replaceAll("\\\"", "");
        JsonArray roles = (JsonArray) result.get("roles");


        List<UserDTO> userDTO = userService.findByUsername("inzent_" + username);

        UserDTO userDTO1 = new UserDTO();

        if (userDTO.isEmpty()) { // 첫 소셜로그인
            System.out.println("처음 로그인 함");
            userDTO1.setUsername("inzent_" + username);
//            userDTO1.setNickname("ROLE_NORMAL");
            userDTO1.setPassword(passwordEncoder.encode("password"));
            userDTO1.setNickname(nickname);
            userDTO1.setToken(token);
            userDTO.add(userDTO1);

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
            userService.update(updateUser);
        }
        UserCustomDetails userDetails = (UserCustomDetails) userCustomDetailsService.loadUserByUsername("inzent_" + username);

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        // 인증 객체를 현재 스레드의 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(auth);

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("logIn", userDetails.getUserDTO());
//
//        if () { // app에서 들어온 요청이라면
//
//        }
        return "redirect:/app/main";
    }

    @GetMapping("/accessTokenCheck")
    public String check(HttpSession session, @AuthenticationPrincipal UserCustomDetails userCustomDetails) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = new UserDTO();
        int statusCodeValue = 0;
        if (principal instanceof UserDetails) {
            //일반로그인
            System.out.println(" 일반로그인 사용자는 인젠트 로그인을 사용하려면 인젠트에 로그인 또는 가입을 해야된다.");
            System.out.println("인젠트 회원가입 화면 보여주기");
            System.out.println("OAuthController.check if userDto ");


            String username = ((UserDetails) principal).getUsername();
            userDTO = userCustomDetails.getUserDTO();
            if (userDTO.getToken().isEmpty()) {
                return "redirect:/inzentRegister";
            } else {
                statusCodeValue = 200;
            }

        } else {
            //인젠트 로그인
            UserDTO logIn = (UserDTO) session.getAttribute("logIn");
            String username = principal.toString();
            userDTO = logIn;
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8000/resource/check";
            String token = userService.findToken(userDTO.getId());
            System.out.println("===================================Token====================");
            System.out.println("token = " + token);
            // Use the access token for authentication
            HttpHeaders headers1 = new HttpHeaders();
            headers1.add("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(headers1);
            ResponseEntity<String> responseResource = null;
            responseResource = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            statusCodeValue = responseResource.getStatusCodeValue();
        }
        System.out.println(userDTO == null);
        if (userDTO == null) {
            userDTO = authLogin(userCustomDetails, session);
        }

        if (statusCodeValue == 200) {
            System.out.println("인증성공");
            // 자산 리스트를 보여주고 선택이 가능한 페이지로 가야됨

            return "redirect:/app/addProperty";
        } else {
            System.out.println("인증실패");
            //인증이 실패하면 다시 main으로 가야됨 ==  로그인의 문제가 있기 떄문, 만약에 풀린거라면 main에서 로그인으로 보내기
            return "redirect:/app/main";
        }
    }

    @GetMapping("/inzentRegister")
    public RedirectView inzentRegister() {
        System.out.println("OAuthController.inzentRegister");

        return new RedirectView("http://localhost:9000/login");
    }

    public UserDTO authLogin(@AuthenticationPrincipal UserCustomDetails userCustomDetails, HttpSession session) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = new UserDTO();
        if (principal instanceof UserDetails) {
            System.out.println("일반로그인");
            //일반로그인
            String username = ((UserDetails) principal).getUsername();
            userDTO = userCustomDetails.getUserDTO();
        } else {
            System.out.println("인젠트로그인");
            //인젠트 로그인
            UserDTO logIn = (UserDTO) session.getAttribute("logIn");
            String username = principal.toString();
            userDTO = logIn;
        }
        return userDTO;

    }

    @GetMapping("/userInfo")
    @ResponseBody
    public String userINfo(@AuthenticationPrincipal UserCustomDetails userCustomDetails) {
        System.out.println("userCustomDetails = " + userCustomDetails);
        System.out.println("userCustomDetails = " + userCustomDetails.getUserDTO());
        return "userINfo";
    }
}