//package com.ipa.openapi_inzent.config;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
//import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//@AllArgsConstructor
//@Configuration
//@Slf4j
//public class WebClientConfig {
//
//    @Bean("AuthProvider")
//        // with spring-boot-starter-web
//    WebClient webClient(
//            ClientRegistrationRepository clientRegistrationRepository,
//            OAuth2AuthorizedClientService authorizedClientService
//    ) {
//        var oauth = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
//                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
//                        clientRegistrationRepository, authorizedClientService
//                )
//        );
//        oauth.setDefaultClientRegistrationId("AuthProvider");
//        return WebClient.builder()
//                .apply(oauth.oauth2Configuration())
//                .build();
//    }
//
//    private ExchangeFilterFunction logRequest() {
//        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
//            log.info("Request: [{}] {}", clientRequest.method(), clientRequest.url());
//            log.debug("Payload: {}", clientRequest.body());
//            return Mono.just(clientRequest);
//        });
//    }
//}
