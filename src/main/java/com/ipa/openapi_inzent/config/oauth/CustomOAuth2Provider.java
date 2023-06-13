//package com.ipa.openapi_inzent.config.oauth;
//
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//
//public enum CustomOAuth2Provider {
//
//    INZNET {
//        public ClientRegistration.Builder getBuilder(String registrationId) {
//            System.out.println("CustomOAuth2Provider.getBuilder=========================");
//            ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.POST, DEFAULT_LOGIN_REDIRECT_URL);
//            builder.scope("clientId");
//            builder.authorizationUri("http://localhost:9000/oauth2/authorize");
//            builder.tokenUri("http://localhost:9000/oauth/token");
//            builder.userInfoUri("https://8000/resource/admin");
//            builder.userNameAttributeName("id");
//            builder.clientName("inzent");
//
////            builder.code_challenge
//            return builder;
//        }
//
//    };
//
//    private static final String DEFAULT_LOGIN_REDIRECT_URL = "http://localhost:8080/authorized";
//
//    protected final ClientRegistration.Builder getBuilder(String registrationId, ClientAuthenticationMethod method, String redirectUri) {
//        ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
//        builder.clientAuthenticationMethod(method);
//        builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
//        builder.redirectUriTemplate(redirectUri);
//        return builder;
//    }
//
//    public abstract ClientRegistration.Builder getBuilder(String registrationId);
//
//}