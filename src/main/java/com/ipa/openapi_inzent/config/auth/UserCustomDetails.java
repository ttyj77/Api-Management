package com.ipa.openapi_inzent.config.auth;

import com.ipa.openapi_inzent.model.UserDTO;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class UserCustomDetails implements UserDetails {
    private UserDTO userDTO;


    // 일반 시큐리티 로그인시 사용
    public UserCustomDetails(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    // 계정의 권한 목록을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(userDTO.getCode()));
        return list;
    }

    @Override
    public String getPassword() {
        return userDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return userDTO.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    } // 계정 만료 여부 리턴

    @Override
    public boolean isAccountNonLocked() {
        return true;
    } // 계정 잠김 여부 리턴

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    } // 비밀번호 만료 여부 리턴

    @Override
    public boolean isEnabled() {
        return true;
    }
}
