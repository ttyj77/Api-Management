package com.ipa.openapi_inzent.service;

import com.ipa.openapi_inzent.dao.UserDao;
import com.ipa.openapi_inzent.model.UserDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private UserDao userDao;

    public UserService(PasswordEncoder passwordEncoder, @Qualifier("userDao") UserDao userDao) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
    }
    public void register(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDao.register(userDTO);
    }
    public UserDTO findByUsername(String username) {
        return userDao.findByUsername(username);
    }
    public UserDTO findByNickname(String nickname) {
        return userDao.findByNickname(nickname);
    }
}
