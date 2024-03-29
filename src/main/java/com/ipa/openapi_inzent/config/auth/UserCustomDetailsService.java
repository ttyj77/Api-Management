package com.ipa.openapi_inzent.config.auth;

import com.ipa.openapi_inzent.model.RoleDTO;
import com.ipa.openapi_inzent.model.UserDTO;
import com.ipa.openapi_inzent.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserCustomDetailsService implements UserDetailsService {

    private final UserService userService;

    public UserCustomDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDTO> userDTO = userService.findByUsername(username);
        if (userDTO.size() == 0) {
            throw new UsernameNotFoundException(username);
        } else {
            UserDTO userDTO2 = userDTO.get(0);
            List<String> roleList = new ArrayList<>();

            for (UserDTO r : userDTO) {
                roleList.add(r.getRole());
            }
            userDTO2.setRoleList(roleList);
            UserCustomDetails details = new UserCustomDetails(userDTO2);
            return details;
        }

    }

}
