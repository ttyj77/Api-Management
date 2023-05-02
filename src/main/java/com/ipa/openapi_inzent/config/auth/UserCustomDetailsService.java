package com.ipa.openapi_inzent.config.auth;

import com.ipa.openapi_inzent.model.UserDTO;
import com.ipa.openapi_inzent.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
}
