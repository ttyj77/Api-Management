package com.ipa.openapi_inzent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {

    @GetMapping("/authorized")
    public void authorized() {
        System.out.println("OAuthController.authorized");
    }

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
}