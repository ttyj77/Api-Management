//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//
//@EnableWebSecurity
//public class MultipleLoginSecurityConfig {
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) {
//        1
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER").and()
//                .withUser("admin").password("password").roles("USER", "ADMIN");
//    }
//
//    @Configuration
//    @Order(1)                                                        2
//    public static class ApiWebSecurityConfigurationAdapter extends FormLoginWebSecurityConfigurerAdapter {
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .antMatcher("/api/**") 3
//                    .authorizeRequests()
//                    .anyRequest().hasRole("ADMIN")
//                    .and()
//                    .httpBasic();
//        }
//    }
//
//    @Configuration                                                   4
//
//    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .authorizeRequests()
//                    .anyRequest().authenticated()
//                    .and()
//                    .formLogin();
//        }
//    }
//}