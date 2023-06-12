package com.ipa.openapi_inzent.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipa.openapi_inzent.config.auth.UserCustomDetails;
import com.ipa.openapi_inzent.config.auth.UserCustomDetailsService;
import io.swagger.annotations.ResponseHeader;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

@Controller
public class OAuthController {


    private UserCustomDetailsService userCustomDetailsService;

    public OAuthController(UserCustomDetailsService userCustomDetailsService) {
        this.userCustomDetailsService = userCustomDetailsService;
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
    public String showEmployees(@RequestParam("code") String code) throws JsonProcessingException, IOException {
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
        String access_token_url = "http://15.165.67.119:9000/oauth2/token";
        access_token_url += "?code=" + code;
        access_token_url += "&grant_type=authorization_code";
        access_token_url += "&redirect_uri=http://52.78.136.223:8080/authorized";
        access_token_url += "&code_verifier=A7MvYn9hmuJQZt7Unepbx9khicAo2IWAAhSCAbeSoA2";
        HttpEntity<String> request = new HttpEntity<String>(headers);
        response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
        System.out.println("Access Token Response ---------" + response.getBody());

        // Get the Access Token From the recieved JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response.getBody());
        String token = node.path("access_token").asText();


        String url = "http://localhost:9000/resource/admin";

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
        // 1. db에 저장하기
//        UserDetails userDetails = userCustomDetailsService.loadUserByUsername(attempt.getUsername());
//
//        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
//        // 인증 객체를 현재 스레드의 SecurityContext에 저장
//        SecurityContextHolder.getContext().setAuthentication(auth);


        return "redirect:/api";
    }

//    private static String getString(String code) throws JsonProcessingException {
//        ResponseEntity<String> response;
//        RestTemplate restTemplate = new RestTemplate();
//
//        // According OAuth documentation we need to send the client id and secret key in the header for authentication
//        String credentials = "client:secret";
//
//
//        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
////
//        System.out.println("encodedCredentials = " + encodedCredentials);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        headers.add("Authorization", "Basic " + encodedCredentials);
////
//        HttpEntity<String> request = new HttpEntity<String>(headers);
//
//        String access_token_url = "http://localhost:9000/oauth2/token";
//        access_token_url += "?code=" + code;
//        access_token_url += "&grant_type=authorization_code";
//        access_token_url += "&redirect_uri=http://127.0.0.1:8000/authorized";
//        access_token_url += "&code_verifier=A7MvYn9hmuJQZt7Unepbx9khicAo2IWAAhSCAbeSoA2";
////
//        response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
////
//        System.out.println("Access Token Response ---------" + response.getBody());
//
//        // Get the Access Token From the recieved JSON response
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode node = mapper.readTree(response.getBody());
//        String token = node.path("access_token").asText();
//
//        String url = "http://localhost:8080/resource/admin";
//
//        // Use the access token for authentication
//        HttpHeaders headers1 = new HttpHeaders();
//        headers1.add("Authorization", "Bearer " + token);
//        HttpEntity<String> entity = new HttpEntity<>(headers1);
//        ResponseEntity<String> responseResource = null;
//        responseResource = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//        System.out.println("responseResource = " + responseResource.getBody());
//        System.out.println("responseResource = " + responseResource.getStatusCodeValue());
//
////        System.out.println(employees);
////        Employee[] employeeArray = employees.getBody();
//
////        ModelAndView model = new ModelAndView("showEmployees");
////        model.addObject("employees", Arrays.asList(employeeArray));
//        return "redirect:/";
//    }


//    // 클라이언트가 구현해야하는 코드 - 발급 받은 코드로 토큰 발행
//    @RequestMapping("/callback")
//    public OauthToken.response code(@RequestParam String code) {
//
//        String cridentials = "clientId:secretKey";
//        // base 64로 인코딩 (basic auth 의 경우 base64로 인코딩 하여 보내야한다.)
//        String encodingCredentials = new String(
//                Base64.encodeBase64(cridentials.getBytes())
//        );
//        String requestCode = code;
//        OauthToken.request.accessToken request = new OauthToken.request.accessToken() {{
//            setCode(requestCode);
//            setGrant_type("authorization_code");
//            setRedirect_uri("http://localhost:8080/callback");
//        }};
//        // oauth 서버에 http 통신으로 토큰 발행
//        OauthToken.response oauthToken = Unirest.post("http://localhost:8080/oauth/token")
//                .header("Authorization", "Basic " + encodingCredentials)
//                .fields(request.getMapData())
//                .asObject(OauthToken.response.class).getBody();
//        return oauthToken;
//    }
//
/*@GetMapping(value = "/authorized")
public ModelAndView showEmployees(@RequestParam("code") String code) throws JsonProcessingException, IOException, UnirestException {
    ResponseEntity<String> response = null;
    System.out.println("Authorization Code------" + code);


    RestTemplate restTemplate = new RestTemplate();

    String cridentials = "client:secret";
    // base 64로 인코딩 (basic auth 의 경우 base64로 인코딩 하여 보내야한다.)
    String encodingCredentials = new String(
            Base64.encodeBase64(cridentials.getBytes())
    );
    String requestCode = code;
    OauthToken.request.accessToken request = new OauthToken.request.accessToken() {{
        setCode(requestCode);
        setGrant_type("authorization_code");
        setRedirect_uri("http://localhost:8080/callback");
    }};
    // oauth 서버에 http 통신으로 토큰 발행
    OauthToken.response oauthToken = Unirest.post("http://localhost:8080/oauth/token")
            .header("Authorization", "Basic " + encodingCredentials)
            .fields(request.getMapData())
            .asObject(OauthToken.response.class).getBody();
    System.out.println("Access Token Response ---------" + response.getBody());
    return null;
}*/
}