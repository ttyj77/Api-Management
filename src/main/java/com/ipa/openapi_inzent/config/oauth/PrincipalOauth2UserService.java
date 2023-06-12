//package com.ipa.openapi_inzent.config.oauth;
//
//
//import com.ipa.openapi_inzent.config.auth.UserCustomDetails;
//import com.ipa.openapi_inzent.model.UserDTO;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        System.out.println("============!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!==================================================");
//        OAuth2User oAuth2User = super.loadUser(userRequest); // google의 회원 프로필 조회
//
//        System.out.println("userRequest = " + userRequest);
//        System.out.println("oAuth2User = " + oAuth2User);
//        return processOAuth2User(userRequest, oAuth2User);
//    }
//
//    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
//        UserDTO user = null;
//        return new UserCustomDetails(user, oAuth2User.getAttributes());
//    }
//
//
//}
