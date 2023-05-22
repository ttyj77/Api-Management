package com.ipa.openapi_inzent.service;

import com.ipa.openapi_inzent.dao.UserDao;
import com.ipa.openapi_inzent.model.UserDTO;
import com.ipa.openapi_inzent.model.UserRoleDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private UserDao userDao;

    public UserService(PasswordEncoder passwordEncoder, @Qualifier("userDao") UserDao userDao) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
    }

    public int register(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userDao.register(userDTO);
        int id = userDTO.getId();
        return id;
    }

    public UserDTO findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public UserDTO findByNickname(String nickname) {
        return userDao.findByNickname(nickname);
    }

    public UserDTO selectOne(int id) {
        return userDao.selectOne(id);
    }

    public void delete(int id) {
        userDao.delete(id);
    }

    public void update(UserDTO userDTO) {
        userDao.update(userDTO);
    }

    public List<UserDTO> selectAll() {
        return userDao.selectAll();
    }

    public void insertRole(UserRoleDTO userRoleDTO) {
        userDao.insertRole(userRoleDTO);
    }
}
