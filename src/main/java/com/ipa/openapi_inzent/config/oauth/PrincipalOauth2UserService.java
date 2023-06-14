//package com.ipa.openapi_inzent.config.oauth;
//
//
//import com.ipa.openapi_inzent.config.auth.UserCustomDetails;
//import com.ipa.openapi_inzent.model.UserDTO;
//import com.ipa.openapi_inzent.service.UserService;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class PrincipalOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//    private UserService userService;
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        System.out.println("============!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!==================================================");
////        OAuth2User oAuth2User = super.loadUser(userRequest); // google의 회원 프로필 조회
//
//        System.out.println("userRequest = " + userRequest);
////        System.out.println("oAuth2User = " + oAuth2User);
////        return processOAuth2User(userRequest, oAuth2User);
//        return null;
//    }
//
//    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
//        UserDTO user = null;
//        // Attribute를 파싱해서 공통 객체로 묶는다. 관리가 편함.
//        OAuth2UserInfo oAuth2UserInfo = null;
////        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
////            System.out.println("구글 로그인 요청~~");
////            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
////        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
////            System.out.println("페이스북 로그인 요청~~");
////            oAuth2UserInfo = new FaceBookUserInfo(oAuth2User.getAttributes());
////        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
////            System.out.println("네이버 로그인 요청~~");
////            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
////        } else {
////            System.out.println("우리는 구글과 페이스북만 지원해요 ㅎㅎ");
////        }
//
////        System.out.println("oAuth2UserInfo.getProvider() : " + oAuth2UserInfo.getProvider());
////        System.out.println("oAuth2UserInfo.getProviderId() : " + oAuth2UserInfo.getProviderId());
//
////        Optional<UserDTO> userOptional =
////                userser.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());
//
//
////        System.out.println("??" + userOptional);
////        if (userOptional.isPresent()) {
////            user = userOptional.get();
////            // user가 존재하면 update 해주기
////            user.setEmail(oAuth2UserInfo.getEmail());
//////			userService.updateUser(user);
////        } else {
//        // user의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음.
//        user = UserDTO.builder()
//                .username("oauth2User")
//                .email(oAuth2UserInfo.getEmail())
//                .role("ROLE_USER")
////                .provider(oAuth2UserInfo.getProvider())
////                .providerId(oAuth2UserInfo.getProviderId())
//                .build();
//        userService.register(user);
//
//        return new UserCustomDetails(user, oAuth2User.getAttributes());
//    }
//
//
////        return new UserCustomDetails(user, oAuth2User.getAttributes());
//}
////
////
////        }
