package com.ipa.openapi_inzent.config.auth;

import com.ipa.openapi_inzent.model.UserDTO;
import com.ipa.openapi_inzent.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserCustomDetailsService implements UserDetailsService {

    private final UserService userService;

    public UserCustomDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = userService.findByUsername(username);
        if (userDTO == null) {
            System.out.println("아이디 정보 없음");
            throw new UsernameNotFoundException(username);
        } else {
            System.out.println("로그인 진행");
            UserCustomDetails details = new UserCustomDetails(userDTO);
            System.out.println(details.getUserDTO());
            return details;
        }

    }

//    @Component
//    public class MyUserDetailsService implements UserDetailsService {
//
//        @Resource
//        private UserService accounts;
//
//        @Override
//        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//            UserDTO account = accounts.findByUsername(username);
//            if(null == account) {
//                throw new UsernameNotFoundException("User " + username + " not found.");
//            }
//            String[] authStrings = new String[0];
//            List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
//            for (int i = 0; i < account.getRole().length(); i++) {
//                authStrings[i] = account.getRole();
//            }
//
//            for(String authString : authStrings) {
//                authorities.add(new SimpleGrantedAuthority(authString));
//            }
//
//            UserDetails ud = new User(account.getUsername(), account.getPassword(), authorities);
//            return ud;
//        }
//
//    }
}
