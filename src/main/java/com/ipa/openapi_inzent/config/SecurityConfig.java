package com.ipa.openapi_inzent.config;

import com.ipa.openapi_inzent.config.auth.UserCustomDetailsService;
import com.ipa.openapi_inzent.handler.UserAuthFailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
// secured Annotation 활성화 / preAuthorize Annotation 활성화
// 일반 메소드에 @Secured("ROLE_ADMIN") 단독 설정 가능 => 신버전
// 일반 메소드에 @PreAuthorize("hasRole('ROLE_USER') or "hasRole('ROLE_ADMIN)") 다중 설정 가능 => 구버전
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Configuration
//    @Order(2)
    public static class App1ConfigurationAdapter {
//        @Bean
//        public AuthenticationSuccessHandler userAuthSuccessHandler() {
//            return new UserAuthSuccessHandler();
//        }

        @Bean
        public AuthenticationFailureHandler userAuthFailureHandler() {
            return new UserAuthFailHandler();
        }

        @Autowired
        private UserCustomDetailsService userDetailsService;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

            http.csrf().disable();
            http.authorizeRequests() //authorizeRequests
                    .antMatchers(HttpMethod.GET, "/error/*", "/login", "/login_proc", "/user/login", "/user/register", "/mydata/**", "/**").permitAll() // 설정된 url은 인증되지 않더라도 누구든 접근 가능
//                .anyRequest().authenticated()// 위 페이지 외 인증이 되어야 접근가능(ROLE에 상관없이)
//                    .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
//                    .antMatchers("/api/**", "/accountList", "/authorization", "/requestPage", "/user/mypage", "/mydata/**").authenticated()
//                    .antMatchers( "/accountList", "/authorization", "/requestPage", "/user/mypage", "/mydata/**").hasRole("PROVIDER")
//                    .antMatchers("/accountList", "/authorization", "/requestPage", "/user/mypage").hasRole("NORMAL")
//                    .antMatchers("/mydata/**").hasRole("ADMIN")
                    .and()
                    .formLogin().loginPage("/user/login")  // 접근이 차단된 페이지 클릭시 이동할 url
                    .loginProcessingUrl("/login-proc") // 로그인시 맵핑되는 url
                    .usernameParameter("username")      // view form 태그 내에 로그인 할 id 에 맵핑되는 name ( form 의 name )
                    .passwordParameter("password")      // view form 태그 내에 로그인 할 password 에 맵핑되는 name ( form 의 name )
//                    .failureUrl("/user/login?error=true&exception=*")
//                    .successHandler(userAuthSuccessHandler()) // 로그인 성공시 실행되는 메소드
                    .failureHandler(userAuthFailureHandler()) // 로그인 실패시 실행되는 메소드
                    .and()
                    .logout()
                    .logoutUrl("/logout")
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/user/login")
//                    .deleteCookies("JSESSIONID").permitAll()
                    .invalidateHttpSession(true); // 세션 clear

            http.rememberMe() // rememberMe 기능 작동함
                    .key("sampleKey") // 필수값
                    .rememberMeParameter("remember-me") // default: remember-me, checkbox 등의 이름과 맞춰야함
                    .tokenValiditySeconds(36000) // 쿠키의 만료시간 설정(초), default: 14일
                    .alwaysRemember(false) // 사용자가 체크박스를 활성화하지 않아도 항상 실행, default: false
                    .userDetailsService(userDetailsService); // 기능을 사용할 때 사용자 정보가 필요함. 반드시 이 설정 필요함.

            return http.build();
        }
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/css/**", "/scss/**", "/vendor/**");
    }
}

